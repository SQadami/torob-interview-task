package ir.torob.imageloader.binding

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions

@BindingAdapter(
    "imageHash",
    "placeHolderDrawable",
    "errorDrawable",
    "circleCropImage",
    "crossFadeImage",
    "overrideImageWidth",
    "overrideImageHeight",
    "cornerRadiusRes",
    "transformation",
    "imageLoadListener",
    requireAll = false
)
fun bindImage(
    imageView: ImageView,
    imageUrl: String?,
    placeHolderDrawable: Int? = null,
    errorDrawable: Int? = null,
    circleCrop: Boolean? = null,
    crossFade: Boolean? = null,
    overrideWidth: Int? = null,
    overrideHeight: Int? = null,
    cornerRadiusRes: Int? = null,
    transformation: Transformation<Bitmap>? = null,
    listener: RequestListener<Drawable>? = null
) =
    imageView.bind(
        imageUrl,
        placeHolderDrawable,
        errorDrawable,
        circleCrop,
        crossFade,
        overrideWidth,
        overrideHeight,
        cornerRadiusRes,
        transformation,
        listener
    )

@SuppressLint("CheckResult")
fun ImageView.bind(
    imageUrl: String? = null,
    placeHolderDrawable: Int? = null,
    errorDrawable: Int? = null,
    circleCrop: Boolean? = null,
    crossFade: Boolean? = null,
    overrideWidth: Int? = null,
    overrideHeight: Int? = null,
    cornerRadiusRes: Int? = null,
    transformation: Transformation<Bitmap>? = null,
    listener: RequestListener<Drawable>? = null
) {
    val withContext = Glide.with(this.context)
    val request =
        if (!imageUrl.isNullOrBlank()) {
            withContext
                .load(imageUrl)
                .also { requestBuilder ->
                    placeHolderDrawable?.let { requestBuilder.placeholder(it) }
                    errorDrawable?.let { requestBuilder.error(it) }
                    listener?.let { requestBuilder.listener(it) }
                    circleCrop?.let { requestBuilder.circleCrop() }
                    overrideWidth?.let { requestBuilder.override(it, overrideHeight!!) }
                    transformation?.let { requestBuilder.transform(it) }
                    cornerRadiusRes?.let {
                        val radius = context.resources.getDimensionPixelOffset(cornerRadiusRes)
                        requestBuilder.apply(cornerRadiusTransform(radius))
                    }
                    crossFade?.let {
                        requestBuilder.transition(DrawableTransitionOptions.withCrossFade())
                    }
                }
        } else {
            withContext.load(placeHolderDrawable)
        }
    request.into(this)
}

fun cornerRadiusTransform(radius: Int) =
    RequestOptions().optionalTransform(
        MultiTransformation(
            CenterCrop(),
            RoundedCorners(radius)
        )
    )