package d.spidchenko.flashcards.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import d.spidchenko.flashcards.R;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        TextView tvWord = (TextView)requireActivity().findViewById(R.id.tvCurrentWord);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getCurrentTranslation().observe(getViewLifecycleOwner(), tvWord::setText);

        tvWord.setOnClickListener(v -> {
            mViewModel.translateToRussian();
        });

        Button btnShow = (Button)requireActivity().findViewById(R.id.btnShow);
        btnShow.setOnClickListener(v -> {
            mViewModel.nextWord();
        });
    }

}