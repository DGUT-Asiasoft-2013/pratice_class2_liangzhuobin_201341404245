package inputcells;


import java.io.ByteArrayOutputStream;

import com.example.hello.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PictureInputCellFragment extends Fragment{
	
	final int REQUESTCODE_CIMERA=1;
	final int REQUESTCODE_ALBUM=2;
	
	ImageView imageView;
	TextView labelText;
	TextView hintText;
	byte[] pngData;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_inputcell_picture, container);
		
		
		
		imageView=(ImageView)view.findViewById(R.id.image);
		labelText=(TextView)view.findViewById(R.id.label);
		hintText=(TextView)view.findViewById(R.id.hint);
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onImageViewClicked();
				
			}
		});
		return view;
	}
	
	
	void onImageViewClicked(){
		String[] items={
				"����",
				"���"
		};
		
		new AlertDialog.Builder(getActivity())
		.setTitle(labelText.getText())
//		.setMessage(hintText.getText())
		.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					takePhoto();
					break;
				case 1:
					pickFromAlbum();
					break;
				default:
					break;
				}
				
			}
		})
		.setNegativeButton("ȡ��", null)
		.show();
	}
			
	void takePhoto(){
		Intent itnt=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(itnt, REQUESTCODE_CIMERA);
	}
	
	void pickFromAlbum(){
		Intent itnt=new Intent(Intent.ACTION_GET_CONTENT);
		itnt.setType("image/*");
		startActivityForResult(itnt, REQUESTCODE_ALBUM);
				
	}
		

	void saveBitmap(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, baos);
		pngData = baos.toByteArray();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_CANCELED) return;
		
		if(requestCode==REQUESTCODE_CIMERA){
			
			Bitmap bmp=(Bitmap)data.getExtras().get("data");
			imageView.setImageBitmap(bmp);

		}else if(requestCode==REQUESTCODE_ALBUM){
			try {
				Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
				imageView.setImageBitmap(bmp);
				saveBitmap(bmp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	public void setLabelText(String labelText){
		this.hintText.setText(labelText);
	}
	
	public void setHintText(String hintText){
		this.hintText.setText(hintText);
	}


	public byte[] getPngData() {
		return pngData;
	}
}
