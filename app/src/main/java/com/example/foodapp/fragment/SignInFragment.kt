package com.example.foodapp.fragment
import UserPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodapp.MainActivity
import com.example.foodapp.databinding.FragmentSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        // Clear text in email and password fields
        binding.editTextEmail.text.clear()
        binding.editTextPassword.text.clear()

        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        binding.btnSignUp.setOnClickListener {
            signUp()
        }

        return binding.root
    }

    private fun signIn() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val user = auth.currentUser
                        (activity as MainActivity).onUserSignedIn()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please enter valid email and password",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun signUp() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (email.isNotBlank() && password.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Sign up success, update UI with the signed-up user's information
                        val user = auth.currentUser
                        // Add display name if needed
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(email) // You can set display name as needed
                            .build()

                        user?.updateProfile(profileUpdates)
                            ?.addOnCompleteListener { profileUpdateTask ->
                                if (profileUpdateTask.isSuccessful) {
                                    // Profile updated
                                    (activity as MainActivity).onUserSignedIn()
                                }
                            }
                    } else {
                        // If sign up fails, display a message to the user.
                        Toast.makeText(context, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Please enter valid email and password",
                Toast.LENGTH_SHORT).show()
        }
    }
}
