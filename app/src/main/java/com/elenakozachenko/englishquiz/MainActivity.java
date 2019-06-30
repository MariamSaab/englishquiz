package com.elenakozachenko.englishquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;


public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler{
    Button mTrueButton;
    Button mFalseButton;
    private Button mNextButton;

    private TextView mQuestionTextView;
    private static final String KEY_INDEX = "index";
    BillingProcessor bp;

    private Question[] mQuestionBank = new Question[]
            {
                    new Question(R.string.question_alphabet, true),
                    new Question(R.string.question_article, true),
                    new Question(R.string.question_week, false),
                    new Question(R.string.question_americans, false),
                    new Question(R.string.question_seasons, true),
                    new Question(R.string.question_holiday, true),
                    new Question(R.string.question_first, true),
                    new Question(R.string.question_magazine, false),
                    new Question(R.string.question_pink, true),
                    new Question(R.string.question_library, false),
                    new Question(R.string.question_feet, false),
                    new Question(R.string.question_bed, false),
                    new Question(R.string.question_goods, true),
                    new Question(R.string.question_hundred, false),
                    new Question(R.string.question_weak, true),
                    new Question(R.string.question_the_worst, true),
                    new Question(R.string.question_litter, true),
                    new Question(R.string.question_mistake, false),
                    new Question(R.string.question_lot_of, true),
                    new Question(R.string.question_fine, true),
                    new Question(R.string.question_must_not, true),
                    new Question(R.string.question_expensive, false),
                    new Question(R.string.question_i_have, true),
                    new Question(R.string.question_clever, false),
                    new Question(R.string.question_saw, true),
                    new Question(R.string.question_daughter, false),
                    new Question(R.string.question_water, false),
                    new Question(R.string.question_swim, false),
                    new Question(R.string.question_now, false),
                    new Question(R.string.question_ask, true),
            };
    private int mCurrentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bp = new BillingProcessor(this, null, this);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);


        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
            });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
            });

        mNextButton = (Button) findViewById(R.id.next_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.purchase(MainActivity.this, "android.test.purchased");
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();

            }
                });
        updateQuestion();

    }
    private void updateQuestion() {
        int question =
                mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int messageResId = 0;
        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
        } else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId,
                Toast.LENGTH_LONG)
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX,
                mCurrentIndex);
    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {


    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, "Something get wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
