package com.appyfurious.appy_picture

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class ImagesAdapter(context: Context) : RecyclerView.Adapter<ImageHolder>() {

    private val placeholder = BitmapFactory.decodeResource(context.resources, R.drawable.abc_ab_share_pack_mtrl_alpha)

    private val list = listOf(
            "https://ic.pics.livejournal.com/center7universe/39721538/1731/1731_900.jpg",
            "https://thumbs.dreamstime.com/z/smiling-woman-standing-pool-134250022.jpg",
            "https://thumbs.dreamstime.com/b/photo-lot-apples-sale-134249959.jpg",
            "https://thumbs.dreamstime.com/z/silhouette-flowers-sunset-134249931.jpg",
            "https://st2.depositphotos.com/2001755/5408/i/450/depositphotos_54081723-stock-photo-beautiful-nature-landscape.jpg",
            "http://xaxa-net.ru/uploads/posts/2018-10/1538509096_krasivye-foto_xaxa-net.ru-2.jpg",
            "https://cdn.trinixy.ru/pics3/20080124/podb/6/krasota_01.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSiBkhvqmDdtAcmtquq-Lb-nD-dZjZEaKAn8n1djSkzLhNM_DiD",
            "https://thumbs-prod.si-cdn.com/qXrJJ-l_jMrQbARjnToD0fi-Tsg=/800x600/filters:no_upscale()/https://public-media.si-cdn.com/filer/d6/93/d6939718-4e41-44a8-a8f3-d13648d2bcd0/c3npbx.jpg",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ-PWVbZ2HPZKomW07kt8L4pT8N6uFxHTJFHzpvVutZQAm5ykenag",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQDDFJm954to5bSOqpOBy87F0sh5hHh_l8SHuKPi1sDcguq6NNv"
    )

    override fun getItemCount() = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val name = list[position]
        holder.bind(name, placeholder!!)
    }
}