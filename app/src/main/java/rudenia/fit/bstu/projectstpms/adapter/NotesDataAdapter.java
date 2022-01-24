package rudenia.fit.bstu.projectstpms.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;

import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import rudenia.fit.bstu.projectstpms.model.Category;
import rudenia.fit.bstu.projectstpms.model.Notes;
import rudenia.fit.bstu.projectstpms.grafics.my_view.PieChartView;


import rudenia.fit.bstu.projectstpms.R;


public class NotesDataAdapter extends RecyclerView.Adapter<NotesDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Notes> costs;
    private Context context;

    public NotesDataAdapter(Context context, List<Notes> costs) {
        this.context = context;
        this.costs = costs;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Notes cost = costs.get(position);

        holder.colorView.setBackgroundColor(PieChartView.getColor(context, cost.getColor()));
        holder.categoryView.setText(cost.getCategoryName());
        holder.sumCostView.setText(cost.getSumCosts());

        holder.bind(costs.get(position));
    }

    @Override
    public int getItemCount() {
        return costs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mCardView;
        final TextView colorView, categoryView, sumCostView;


        ViewHolder(View view) {
            super(view);
            colorView =  view.findViewById(R.id.color_rect);
            categoryView =  view.findViewById(R.id.category_name);
            sumCostView =  view.findViewById(R.id.sum_cost);
            mCardView = view.findViewById(R.id.cardView);
        }

        public void bind(final Notes item) {
        }
    }
}
