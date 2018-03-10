package colon.jonathan.buttoncodechallenge.ui.home.createUser

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import colon.jonathan.buttoncodechallenge.BR
import colon.jonathan.buttoncodechallenge.R
import colon.jonathan.buttoncodechallenge.databinding.FragmentCreateUserBinding
import colon.jonathan.buttoncodechallenge.injection.DaggerViewModelFactory
import colon.jonathan.buttoncodechallenge.ui.home.HomeViewModel
import dagger.android.support.AndroidSupportInjection
import toast
import javax.inject.Inject


class CreateUserFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    private lateinit var viewDataBinding: FragmentCreateUserBinding

    lateinit var viewModel: HomeViewModel

    @LayoutRes
    private val layoutId = R.layout.fragment_create_user

    private val bindingVariable = BR.viewModel

    private lateinit var rootView: View


    override fun onAttach(context: Context?) {
        performDependencyInjection()
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        viewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = viewDataBinding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding.setVariable(bindingVariable, viewModel)
        viewDataBinding.executePendingBindings()
        setUpCreateUserButton()
    }

    private fun performDependencyInjection() {
        AndroidSupportInjection.inject(this)
    }

    private fun setUpCreateUserButton() {
        viewDataBinding.btnCreateUser.setOnClickListener({
            createUser()
        })
    }

    private fun createUser() {
        if (viewDataBinding.etName.text.isNotEmpty() && viewDataBinding.etEmail.text.isNotEmpty()) {
            val name = viewDataBinding.etName.text.toString()
            val email = viewDataBinding.etEmail.text.toString()
            viewModel.createUser(name, email)
        } else {
            activity?.toast(getString(R.string.error_incomplete_forms))
        }
    }

    companion object {
        const val TAG = "CreateUserFragment"
        fun newInstance(): CreateUserFragment {
            return CreateUserFragment()
        }
    }
}
