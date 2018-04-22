package alpay.com.interactiveexperiments.camera;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import alpay.com.interactiveexperiments.R;
import alpay.com.interactiveexperiments.camera.Components.BarcodeCaptureActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class TeachingCardsFragment extends Fragment {

    private static final String LOG_TAG = TeachingCardsFragment.class.getSimpleName();
    private static final int BARCODE_READER_REQUEST_CODE = 1;
    View view;
    Unbinder unbinder;

    @BindView(R.id.result_textview)
    TextView mResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_teachingcards, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.scan_barcode_button)
    public void scanBarcode(){
        Intent intent = new Intent(getActivity().getApplicationContext(), BarcodeCaptureActivity.class);
        startActivityForResult(intent, BARCODE_READER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_READER_REQUEST_CODE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    if(barcode != null){
                        String url = barcode.displayValue;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                } else mResultTextView.setText(R.string.no_barcode_captured);
            } else Log.e(LOG_TAG, "Barcode error");
        } else super.onActivityResult(requestCode, resultCode, data);
    }
}

