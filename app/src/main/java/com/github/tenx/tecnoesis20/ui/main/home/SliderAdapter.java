package com.github.tenx.tecnoesis20.ui.main.home;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.bumptech.glide.Glide;
        import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
        import com.github.tenx.tecnoesis20.R;
        import com.smarteist.autoimageslider.SliderViewAdapter;
        import com.stfalcon.frescoimageviewer.ImageViewer;

        import java.util.ArrayList;
        import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> imageUrls;


    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        notifyDataSetChanged();
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_imageslider_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        String currentImage = imageUrls.get(position);

        Glide.with(context).load(currentImage).placeholder(R.drawable.placeholder_image).into(viewHolder.imageViewBackground);
        viewHolder.imageViewBackground.setOnClickListener(v -> {
            GenericDraweeHierarchyBuilder hierarchyBuilder = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setFailureImage(R.drawable.placeholder_image)
                    .setProgressBarImage(R.drawable.placeholder_image)
                    .setPlaceholderImage(R.drawable.placeholder_image);
            new ImageViewer.Builder(context, imageUrls)
                    .setStartPosition(position).setCustomDraweeHierarchyBuilder(hierarchyBuilder)
                    .show();
        });
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return imageUrls == null ? 0 : imageUrls.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView textViewDescription;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.imageView);

            this.itemView = itemView;
        }
    }
}