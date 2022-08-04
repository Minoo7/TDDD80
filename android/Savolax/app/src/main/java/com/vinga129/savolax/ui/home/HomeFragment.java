package com.vinga129.savolax.ui.home;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import androidx.annotation.NonNull;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.ViewModelProvider;
import com.vinga129.savolax.MainActivity;
import com.vinga129.savolax.R;
import com.vinga129.savolax.base.AnnotationUtil.AnnotationContentId;
import com.vinga129.savolax.base.BaseFragment;
import com.vinga129.savolax.base.BaseRecyclerAdapter;
import com.vinga129.savolax.custom.CustomNullableIntegerArgument;
import com.vinga129.savolax.databinding.FragmentHomeBinding;
import com.vinga129.savolax.other.MiniCustomersRecyclerAdapter;
import com.vinga129.savolax.retrofit.rest_objects.MiniCustomer;
import com.vinga129.savolax.retrofit.rest_objects.Post;
import java.util.ArrayList;
import java.util.List;

@AnnotationContentId(contentId = R.layout.fragment_home)
public class HomeFragment extends BaseFragment<FragmentHomeBinding>
        implements BaseRecyclerAdapter.OnItemListener {

    private List<MiniCustomer> allCustomers;
    private MiniCustomersRecyclerAdapter miniCustomersRecyclerAdapter;

    //@SuppressWarnings("ConstantConditions")
    @Override
    protected void initFragment() {
        HomeViewModel homeViewModel = new HomeViewModel(user.getId());
        binding.setViewmodel(homeViewModel);
        PostItemRecyclerAdapter postItemRecyclerAdapter = new PostItemRecyclerAdapter(this);
        if (navController == null)
            reInitNavController();
        miniCustomersRecyclerAdapter = new MiniCustomersRecyclerAdapter((pos, data) -> {
            if (navController == null)
                reInitNavController();
            navController.navigate(HomeFragmentDirections.toProfile()
                    .setCustomerId(new CustomNullableIntegerArgument(((MiniCustomer) data).getId())));
        });

        binding.setMiniCustomersAdapter(miniCustomersRecyclerAdapter);
        binding.setPostItemAdapter(postItemRecyclerAdapter);

        SearchViewModel searchViewModel = new ViewModelProvider(this)
                .get(SearchViewModel.class);

        searchViewModel.getCustomers().observe(getViewLifecycleOwner(), miniCustomers -> {
            allCustomers = miniCustomers;
            miniCustomersRecyclerAdapter.updateData(allCustomers);
        });

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull final Menu menu, @NonNull final MenuInflater menuInflater) {
                menu.clear();
                menuInflater.inflate(R.menu.search_customers, menu);

                // below line is to get our menu item.
                MenuItem searchItem = menu.findItem(R.id.search);

                SearchView searchView = (SearchView) searchItem.getActionView();

                searchView.setOnCloseListener(() -> {
                    homeViewModel.getShowSearch().set(false);
                    return false;
                });

                searchView.setOnQueryTextListener(new OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(final String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(final String s) {
                        // inside on query text change method we are
                        // calling a method to filter our recycler view.
                        filter(s);
                        return false;
                    }
                });

                searchItem.setOnActionExpandListener(new OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(final MenuItem menuItem) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(final MenuItem menuItem) {
                        homeViewModel.getShowSearch().set(false);
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull final MenuItem menuItem) {
                homeViewModel.getShowSearch().set(true);
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void filter(String text) {
        List<MiniCustomer> filteredList = new ArrayList<>();

        // running a for loop to compare elements.
        for (MiniCustomer customer : allCustomers) {
            // checking if the entered string matched with any item of our recycler view.
            if (customer.getUsername().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredList.add(customer);
            }
        }
        miniCustomersRecyclerAdapter.updateData(filteredList);
    }

    @Override
    public void onItemClick(int pos, Object data) {
        if (navController == null)
            reInitNavController();
        navController.navigate(HomeFragmentDirections.toPost((Post) data));
    }

    private void reInitNavController() {
        navController = ((MainActivity) requireActivity()).getNavController();
    }
}