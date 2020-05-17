package com.king.cmccdrama.Utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

public class ImageLoadOptions {

    /**
     * 初始化ImageLoad
     * @param cacheDir 缓存路径
     */
    public static ImageLoaderConfiguration getImageLoaderConfiguration(Context context, File cacheDir) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3)// 线程池内加载的数量,default = 3
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(10 * 1024 * 1024))//自定义内存的缓存策略
                .memoryCacheSize(10 * 1024 * 1024)
                .memoryCacheSizePercentage(13)// default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)//缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())// default
                .imageDownloader(new BaseImageDownloader(context))// default
                .imageDecoder(new BaseImageDecoder(true))// default
                .writeDebugLogs()
                .build();
        return config;
    }

    /**
     * 图片显示时配置
     * @return
     */
    public static DisplayImageOptions getDisplayImageOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置是否缓存在SD卡中
                .considerExifParams(true)//是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片的缩放类型
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                .build();
        return options;
    }
}
