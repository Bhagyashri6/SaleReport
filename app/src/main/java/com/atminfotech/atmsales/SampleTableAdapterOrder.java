package com.atminfotech.atmsales;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.atminfotech.atmsales.Order.orderlist;

/**
 * This class implements the main functionalities of the TableAdapter in
 * Mutuactivos.
 * 
 * 
 * @author Brais Gabín
 */
public abstract class SampleTableAdapterOrder extends BaseTableAdapter {
	private final Context context;
	private final LayoutInflater inflater;
	private ArrayList<ModelStockReport> list = new ArrayList<>();

	/**
	 * Constructor
	 *
	 * @param context
	 *            The current context.
	 */
	public SampleTableAdapterOrder(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	/**
	 * Returns the context associated with this array adapter. The context is
	 * used to create views from the resource passed to the constructor.
	 * 
	 * @return The Context associated with this adapter.
	 */
	public Context getContext() {
		return context;
	}

	/**
	 * Quick access to the LayoutInflater instance that this Adapter retreived
	 * from its Context.
	 * 
	 * @return The shared LayoutInflater.
	 */
	public LayoutInflater getInflater() {
		return inflater;
	}

	@Override
	public View getView(final  int row, final int column, View converView, ViewGroup parent) {
		if (converView == null) {
			converView = inflater.inflate(getLayoutResource(row, column), parent, false);
		}
		setText(converView, getCellString(row, column));
        TextView cellString = (TextView) converView.findViewById(android.R.id.text1);
        cellString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(context, getCellString(row, -1), Toast.LENGTH_SHORT).show();
				if(row==-1) {
					Toast.makeText(getContext(),getCellString(row,column),Toast.LENGTH_SHORT).show();
				}
				else {


					Intent intent = new Intent(getContext(), OrderDetail.class);
					intent.putExtra("Bill", orderlist.get(row).getItemcode());
					context.startActivity(intent);
				}
            }
        });
		return converView;
	}

	/**
	 * Sets the text to the view.
	 * 
	 * @param view
	 * @param text
	 */
	private void setText(View view, String text) {
		((TextView) view.findViewById(android.R.id.text1)).setText(text);
	}

	/**
	 * @param row
	 *            the title of the row of this header. If the column is -1
	 *            returns the title of the row header.
	 * @param column
	 *            the title of the column of this header. If the column is -1
	 *            returns the title of the column header.
	 * @return the string for the cell [row, column]
	 */
	public abstract String getCellString(int row, int column);

	public abstract int getLayoutResource(int row, int column);



}
