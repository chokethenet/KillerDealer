package net.chokethe.killerdealer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import net.chokethe.killerdealer.adapters.BlindsAdapter;
import net.chokethe.killerdealer.holders.BlindsConfigHolder;

public class BlindsConfigActivity extends AppCompatActivity {

    private BlindsConfigHolder mBlindsConfigHolder;
    private BlindsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blinds_config);

        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.showToast(BlindsConfigActivity.this, "ADD BLIND");
                mAdapter.getBlinds().add(0);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBlindsConfigHolder = new BlindsConfigHolder(this);
        updateUI();
    }

    private void updateUI() {
        // TODO: update the blind result textview with a pretty print from the lists

        mAdapter = new BlindsAdapter(this, mBlindsConfigHolder.getBlindsListPref());
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_blinds);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // FIXME: usar estilos más pequeños y variables con los circulos?
        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Integer blind = (Integer) viewHolder.itemView.getTag();
                mAdapter.getBlinds().remove(blind);
                mAdapter.notifyDataSetChanged();
                MainActivity.showToast(BlindsConfigActivity.this, "DELETE BLIND");
            }
        }).attachToRecyclerView(mRecyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBlindsConfigHolder.setBlindsListPref(mAdapter.getBlinds());
        mBlindsConfigHolder.save(this);
    }

}
