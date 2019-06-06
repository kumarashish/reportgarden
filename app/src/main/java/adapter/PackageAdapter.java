package adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import interfaces.OnImportClick;
import model.DependencyModel;
import model.SearchModel;
import reportgarden.toppackapp.R;

public class PackageAdapter extends BaseAdapter {
    List<DependencyModel> listItems;

    Activity act;

    public PackageAdapter(List<DependencyModel> listItems, Activity act) {
        this.listItems = listItems;
        this.act = act;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final DependencyModel model = listItems.get(position);
        ViewHolder holder = null;
        if (convertView == null) {

            LayoutInflater inflater = act.getLayoutInflater();
            convertView = inflater.inflate(R.layout.package_items, null, true);
            holder = new ViewHolder(convertView);

        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.name.setText(model.getDependency());
        holder.count.setText("Count : " + model.getCount());

        convertView.setTag(holder);
        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.count)
        TextView count;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}