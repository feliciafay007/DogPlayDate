package project.coen268.scu.dogplaydate;

import android.content.Context;
import android.text.style.RasterizerSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * wenyi
 */

public class DatesRecordAdapter extends ArrayAdapter<DatesRecordEntity>{
//public class DatesRecordAdapter extends ArrayAdapter<String>{

    private final List<DatesRecordEntity> listSource;
    //private final List<String> listSource;

   public DatesRecordAdapter(Context context, int resource, List<DatesRecordEntity> listsrc) {
   //public DatesRecordAdapter(Context context, int resource, List<String> listsrc) {
        super(context, resource, listsrc);
        this.listSource = listsrc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //todo: 30% done, complete this part
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.row_playdateslist, null);
        TextView startTimeTextView = (TextView) row.findViewById(R.id.startTimeTextView);
        TextView endTimeTextView = (TextView) row.findViewById(R.id.endTimeTextView);
        TextView timeDurationTextView = (TextView) row.findViewById(R.id.timeDurationTextView);
        ImageButton dogButton = (ImageButton) row.findViewById(R.id.dogImageButton);
        ImageButton parkButton = (ImageButton) row.findViewById(R.id.parkImageButton);
        TextView participantTextView = (TextView) row.findViewById(R.id.participantTextView);
        ImageButton invitationStatusImageButton = (ImageButton) row.findViewById(R.id.invitationStatusImageButton);

        startTimeTextView.setText(listSource.get(position).getStartTime().toString().substring(0,5));
        endTimeTextView.setText(listSource.get(position).getEndTime().toString().substring(0,5));
//        startTimeTextView.setText("2020");
//        endTimeTextView.setText("2021");
        long timeDuration = listSource.get(position).getEndTime().getTime() - listSource.get(position).getStartTime().getTime();
        timeDurationTextView.setText(Long.toString(timeDuration));
        participantTextView.setText("zizi");

        //below: logical test, no busines sense
//        startTimeTextView.setText("111");
//        endTimeTextView.setText("222");
//        timeDurationTextView.setText("333");
        dogButton.setImageResource(R.drawable.dog_icon);
        parkButton.setImageResource(R.drawable.common_signin_btn_icon_light);
        invitationStatusImageButton.setImageResource(R.drawable.abc_btn_radio_material);
        return row;
    }
}

/**

public class CustomAdapter extends ArrayAdapter<Person> {
    private final List<Person> persons;

    public CustomAdapter(Context context, int resource, List<Person> persons) {
        super(context, resource, persons);
        this.persons = persons;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_row, null);
        TextView textView = (TextView) row.findViewById(R.id.rowText);
        textView.setText(persons.get(position).getName());
        ImageView imageView = (ImageView) row.findViewById(R.id.rowImage);
        imageView.setImageResource(persons.get(position).getResIDThumbnail());
        return row;
    }
}
*/