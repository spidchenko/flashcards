package d.spidchenko.flashcards.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import d.spidchenko.flashcards.R;
import d.spidchenko.flashcards.VoiceSynthesizer;

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

        TextView tvWord = requireActivity().findViewById(R.id.tvCurrentWord);

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getCurrentTranslation().observe(getViewLifecycleOwner(), tvWord::setText);

        tvWord.setOnClickListener(v -> {
            mViewModel.translate();
        });

        Button btnShow = requireActivity().findViewById(R.id.btnShow);
        btnShow.setOnClickListener(v -> mViewModel.nextWord());

        ImageButton btnIncreaseRate = requireActivity().findViewById(R.id.btnIncreaseRate);
        ImageButton btnDecreaseRate = requireActivity().findViewById(R.id.btnDecreaseRate);

        btnIncreaseRate.setOnClickListener(v -> {
            mViewModel.increaseRate();
            mViewModel.nextWord();
        });

        btnDecreaseRate.setOnClickListener(v -> {
            mViewModel.decreaseRate();
            mViewModel.nextWord();
        });
        VoiceSynthesizer voice = new VoiceSynthesizer(getActivity().getApplication());
    }

}