package com.driveinto.ladyj.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController

import com.driveinto.ladyj.R
import com.driveinto.ladyj.app.AbstractFragment
import com.driveinto.ladyj.app.ServiceLocator
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.util.*

class LoginFragment : AbstractFragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var client: GoogleSignInClient
    private lateinit var manager: CallbackManager
    private lateinit var facebook: LoginButton
    private var identityProvider: IdentityProviders? = null
    private var startDestination: Int = R.id.nav_customer

    private val viewModel: LoginViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {
                val repository = ServiceLocator.instance(activity!!).getLoginRepository()

                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(repository) as T
            }
        }
    }

    private fun setCredential(credential: AuthCredential) {
        activity?.run {
            auth.signInWithCredential(credential).addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    viewModel.signedIn(auth.currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", it.exception)
                    showSnackBar(login, "Authentication Failed.")
                    viewModel.signedIn(null)
                }

                hideProgressBar()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        manager = CallbackManager.Factory.create()

        activity?.run {
            // Configure Google Sign In
            val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
            client = GoogleSignIn.getClient(this, options)

            // Initialize Facebook Login button
            facebook = LoginButton(this)
            facebook.setPermissions("email", "public_profile")
            facebook.fragment = this@LoginFragment
            facebook.registerCallback(manager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    showProgressBar()

                    val credential = FacebookAuthProvider.getCredential(result!!.accessToken.token)
                    setCredential(credential)
                }

                override fun onCancel() {
                    Log.d(TAG, "facebook:onCancel")
                    viewModel.signedIn(null)
                }

                override fun onError(error: FacebookException?) {
                    Log.d(TAG, "facebook:onError", error)
                    viewModel.signedIn(null)
                }
            })
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    private lateinit var login: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        arguments?.let {
            val value = it.getInt("startDestination")
            if (value != 0) {
                startDestination = value
            }
        }

        setProgressBar(view.progressBar)

        login = view.login

        // Set up Adapter
        context?.run {
            val adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, IdentityProviders.values())
            view.identity.adapter = adapter
            // 登入
            view.identity.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                identityProvider = IdentityProviders.fromPosition(position)
                when (identityProvider) {
                    IdentityProviders.Google -> {
                        val signInIntent = client.signInIntent
                        startActivityForResult(signInIntent, RC_SIGN_IN)
                    }
                    IdentityProviders.Facebook -> {
                        LoginManager.getInstance()
                            .logInWithReadPermissions(this@LoginFragment, listOf("public_profile"))
                        facebook.performClick()
                    }
                    else -> {
                        // Could add custom scopes here
                        val scopes = ArrayList<String>()

                        // Examples of provider ID: apple.com (Apple), microsoft.com (Microsoft), yahoo.com (Yahoo)
                        identityProvider?.run {
                            auth.startActivityForSignInWithProvider(
                                activity!!, OAuthProvider.newBuilder(this.providerId, auth).setScopes(scopes).build()
                            ).addOnSuccessListener {
                                viewModel.signedIn(it.user)
                            }.addOnFailureListener {
                                Log.w(TAG, "onCreateView:onFailure", it)
                                showToast(getString(R.string.error_sign_in_failed))
                            }
                        }
                    }
                }
            }
        }

        // 登出
        view.signOut.setOnClickListener {
            // Firebase sign out
            auth.signOut()

            when (identityProvider) {
                IdentityProviders.Google -> {
                    activity?.run {
                        client.signOut().addOnCompleteListener(this) {
                            viewModel.signedIn(null)
                        }
                    }
                }
                IdentityProviders.Facebook -> {
                    LoginManager.getInstance().logOut()
                    viewModel.signedIn(null)
                }
                else -> {
                    viewModel.signedIn(null)
                }
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (startDestination == R.id.nav_login) {
            view.signOut.performClick()
        }

        viewModel.userLoginInfo.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                view.status.text = getString(R.string.generic_status_fmt, it.userName, it.phoneNumber)
                view.detail.text = getString(R.string.firebase_status_fmt, it.providerKey)

                viewModel.login(it)
            } else {
                view.status.setText(R.string.signed_out)
                view.detail.text = null

                view.provider.visibility = View.VISIBLE
                view.signOut.visibility = View.GONE

                hideProgressBar()
            }
        })
        viewModel.loginResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            view.detail.text = getString(R.string.server_status_fmt, it.message)

            view.provider.visibility = View.GONE
            view.signOut.visibility = View.VISIBLE

            hideProgressBar()

            val controller = findNavController(activity!!, R.id.nav_login_controller)
            val action = LoginFragmentDirections.actionNavLoginToNavDefault(it.result, startDestination)
            controller.navigate(action)
        })

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        // Look for a pending auth result
        val pending = auth.pendingAuthResult
        if (pending != null) {
            pending.addOnSuccessListener {
                viewModel.signedIn(it.user)
            }.addOnFailureListener {
                Log.w(TAG, "checkPending:onFailure", it)
            }
        } else {
            Log.d(TAG, "checkPending: null")

            // Check if user is signed in (non-null) and update UI accordingly.
            val currentUser = auth.currentUser
            viewModel.signedIn(currentUser)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Pass the activity result back to the Facebook SDK
        if (identityProvider == IdentityProviders.Facebook) {
            manager.onActivityResult(requestCode, resultCode, data)
        }

        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)

                showProgressBar()

                val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
                setCredential(credential)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                viewModel.signedIn(null)
            }
        }
    }

    companion object {
        private const val TAG = "LoginFragment"
        private const val RC_SIGN_IN = 9001
    }
}
