package net.lapasa.fleetingdreamrecorder.comments;

import com.androidquery.AQuery;

import net.lapasa.fleetingdreamrecorder.R;
import net.lapasa.fleetingdreamrecorder.models.DreamRecording;
import net.lapasa.fleetingdreamrecorder.models.DreamRecordingsModel;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

public class CommentEntryFragment extends Fragment
{
	private AQuery aq;
	private EditText commenEditText;
	private DreamRecordingsModel model;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		model = DreamRecordingsModel.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.comment_entry, container, false);
		aq = new AQuery(view);
		aq.id(R.id.addBtn).clicked(getAddBtnClickListener());
		commenEditText = aq.id(R.id.commentEntryEditText).getEditText();
		return view;
	}

	private OnClickListener getAddBtnClickListener()
	{
		return new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				addComment(commenEditText.getText().toString());
				commenEditText.setText(null);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Feature Not Available");
				builder.setMessage("Coming Soon!");
				builder.show();
			}
		};
	}
	
	private void addComment(String body)
	{
		DreamRecording selectedRecording = model.getSelectedRecording();
		selectedRecording.addComment(body);
	}
	
	
	
}
