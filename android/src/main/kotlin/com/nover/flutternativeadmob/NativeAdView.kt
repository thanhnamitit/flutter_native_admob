package com.nover.flutternativeadmob

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView

enum class NativeAdmobType {
    full, banner, conversationHome
}

class NativeAdView @JvmOverloads constructor(
        context: Context,
        type: NativeAdmobType,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var options = NativeAdmobOptions()
        set(value) {
            field = value
            updateOptions()
        }

    private val adView: UnifiedNativeAdView

    private val ratingBar: RatingBar?

    private val adMedia: MediaView?

    private val adHeadline: TextView?
    private val adAdvertiser: TextView?
    private val adBody: TextView?
    private val adPrice: TextView?
    private val adStore: TextView?
    private val adAttribution: TextView?
    private val callToAction: TextView?

    init {
        val inflater = LayoutInflater.from(context)
        val layout = when (type) {
            NativeAdmobType.full -> R.layout.native_admob_full_view
            NativeAdmobType.banner -> R.layout.native_admob_custom_banner_view
            else -> R.layout.conversation_home
        }
        inflater.inflate(layout, this, true)

        setBackgroundColor(Color.TRANSPARENT)
        adView = findViewById(R.id.ad_view)
        adMedia = adView.findViewById(R.id.ad_media)
        adHeadline = adView.findViewById(R.id.ad_headline)
        adAdvertiser = adView.findViewById(R.id.ad_advertiser)
        adBody = adView.findViewById(R.id.ad_body)
        adPrice = adView.findViewById(R.id.ad_price)
        adStore = adView.findViewById(R.id.ad_store)
        adAttribution = adView.findViewById(R.id.ad_attribution)
        ratingBar = adView.findViewById(R.id.ad_stars)
        val color = if (type == NativeAdmobType.conversationHome) "#676767" else "#676767"
        adAttribution.background = Color.parseColor(color).toRoundedColor(3f)
        callToAction = adView.findViewById(R.id.ad_call_to_action)

        initialize()
    }

    private fun initialize() {
        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        adView.mediaView = adMedia
        // Register the view used for each individual asset.
        adView.headlineView = adHeadline
        adView.bodyView = adBody
        adView.callToActionView = callToAction
        adView.iconView = adView.findViewById(R.id.ad_icon)
        adView.priceView = adPrice
        // adView.starRatingView = ratingBar
        adView.storeView = adStore
        adView.advertiserView = adAdvertiser
    }

    fun setNativeAd(nativeAd: UnifiedNativeAd?) {
        if (nativeAd == null) return

        // Some assets are guaranteed to be in every UnifiedNativeAd.
        adMedia?.setMediaContent(nativeAd.mediaContent)
        adMedia?.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE)

        adHeadline?.text = nativeAd.headline
        adBody?.text = nativeAd.body
        (adView.callToActionView as TextView).text = nativeAd.callToAction
        if (nativeAd.callToAction == null) {
            callToAction?.visibility = View.VISIBLE
        } else {
            callToAction?.visibility = View.GONE
        }
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        val icon = nativeAd.icon

        if (nativeAd.price == null) {
            adPrice?.visibility = View.INVISIBLE
        } else {
            adPrice?.visibility = View.VISIBLE
            adPrice?.text = nativeAd.price
        }

        if (nativeAd.store == null) {
            adStore?.visibility = View.INVISIBLE
        } else {
            adStore?.text = nativeAd.store
        }

        if (nativeAd.advertiser == null) {
            adAdvertiser?.visibility = View.INVISIBLE
        } else {
            adAdvertiser?.visibility = View.VISIBLE
            adAdvertiser?.text = nativeAd.advertiser
        }


        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd)
    }

    private fun updateOptions() {
        adMedia?.visibility = if (options.showMediaContent) View.VISIBLE else View.GONE
        ratingBar?.progressDrawable
                ?.setColorFilter(options.ratingColor, PorterDuff.Mode.SRC_ATOP)

        options.adLabelTextStyle.backgroundColor?.let {
            adAttribution?.background = it.toRoundedColor(4f)
        }

        adHeadline?.setTextColor(options.headlineTextStyle.color)
        adHeadline?.visibility = options.headlineTextStyle.visibility

        adAdvertiser?.setTextColor(options.advertiserTextStyle.color)
        adAdvertiser?.visibility = options.advertiserTextStyle.visibility

        adBody?.setTextColor(options.bodyTextStyle.color)
        adBody?.visibility = options.bodyTextStyle.visibility

        adStore?.setTextColor(options.storeTextStyle.color)
        adStore?.visibility = options.storeTextStyle.visibility

        adPrice?.setTextColor(options.priceTextStyle.color)
        adPrice?.visibility = options.priceTextStyle.visibility

        callToAction?.setTextColor(options.callToActionStyle.color)
        callToAction?.textSize = options.callToActionStyle.fontSize
//        options.callToActionStyle.backgroundColor?.let {
//            callToAction.setBackgroundColor(it)
//        }
    }
}