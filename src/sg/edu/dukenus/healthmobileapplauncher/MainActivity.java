package sg.edu.dukenus.healthmobileapplauncher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	// debugging
	private final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ListView listview = (ListView) findViewById(R.id.ListDukeNUSApps);

		final PackageManager pm = getPackageManager();
		// get a list of installed apps.
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		String currentPkgName = getApplicationContext().getPackageName();
		final ArrayList<ApplicationInfo> list = new ArrayList<ApplicationInfo>();
		
		for (int i = 0; i < packages.size(); ++i) {
			ApplicationInfo info = packages.get(i);
			String packageName = info.packageName;
			if (packageName.startsWith("sg.edu.dukenus.")
					&& !packageName.equals(currentPkgName)) {
				
				String appName = pm.getApplicationLabel(info).toString();
				Log.w(TAG, "app name: "+appName+" and package name: "+info.packageName);
				list.add(info);
			}
		}

		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				R.layout.app_list_item, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final ApplicationInfo item = (ApplicationInfo) parent.getItemAtPosition(position);
				Log.w(TAG, "item is "+item.packageName);
				// TODO launch the corresponding activity
				Intent intent = getPackageManager().getLaunchIntentForPackage(
						item.packageName);
				startActivity(intent);

				/*
				 * view.animate().setDuration(2000).alpha(0) .withEndAction(new
				 * Runnable() {
				 * 
				 * @Override public void run() { list.remove(item);
				 * adapter.notifyDataSetChanged(); view.setAlpha(1); } });
				 */
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class StableArrayAdapter extends ArrayAdapter<ApplicationInfo> {

		//HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();
		ArrayList<ApplicationInfo> apps;
		private final Activity context;


		public StableArrayAdapter(Activity context, int itemResourceId, ArrayList<ApplicationInfo> apps) {
			super(context, itemResourceId, apps);
			this.context = context;
			this.apps = apps;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView = convertView;
			String appName = "";
			String packageName = "";
			PackageManager pm = getPackageManager();
			
			if (rowView == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				rowView = inflater.inflate(R.layout.app_list_item, null);
				ViewHolder viewHolder = new ViewHolder(rowView);
				
				appName = pm.getApplicationLabel(apps.get(position)).toString();
				viewHolder.getAppLabel().setText(appName);
				
				packageName = apps.get(position).packageName;
				viewHolder.getPkgName().setText(packageName);
				
				rowView.setTag(viewHolder);
			}

			ViewHolder holder = (ViewHolder) rowView.getTag();
			
			String s = pm.getApplicationLabel(apps.get(position)).toString();
			holder.getAppLabel().setText(s);
			
			packageName = apps.get(position).packageName;
			holder.getPkgName().setText(packageName);

			return rowView;
		}
		
		class ViewHolder {
			public View row;
			public TextView appLabel;
			public TextView pkgName;
			
			//public ImageView image;
			
			public ViewHolder(View row) { this.row = row; }
			
			public TextView getAppLabel() { 
				if (this.appLabel == null) {
					this.appLabel = (TextView) this.row.findViewById(R.id.AppName); 
				}
				return this.appLabel;
			} 
			
			public TextView getPkgName() {
				if (this.pkgName ==null) {
					this.pkgName = (TextView) this.row.findViewById(R.id.PackageName);
				}
				return this.pkgName;
			}
			
		}

	}

}
