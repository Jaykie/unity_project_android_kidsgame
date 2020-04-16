package com.moonma.common;

import android.app.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast; 
import android.content.Intent;
import android.app.AlertDialog;

import com.moonma.common.IIAPBase;
import com.moonma.common.IIAPBaseListener;

//google in-app billing
import com.moonma.common.google.iab.IabHelper;
import com.moonma.common.google.iab.IabResult;
import com.moonma.common.google.iab.Inventory;
import com.moonma.common.google.iab.Purchase;

import org.json.JSONObject;

import java.util.UUID;


public class IAPGoogle implements IIAPBase {//,OnPayProcessListener,OnLoginProcessListener

    // 自定
    private static String TAG = "IAP";

    Activity mainActivity;
    IIAPBaseListener iapBaseListener;
 
    String strProduct;
    boolean isConsumeProduct;

    IabHelper mHelper;

     /**
     * 谷歌内购- 加密串, 填写你自己谷歌上的RSA加密许可
     */
    private String base64EncodedPublicKey = "";
    /**
     * 内购产品唯一id, 填写你自己添加的内购商品id
     */
    private String SKU = "";//

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    /**
     * 支付附加项- 这里放的是订单号
     */
    private String mDeveloperPayload = "201803131027009010";
    /**
     * 谷歌服务是否已正常安装
     */
    private boolean isGooglePlaySetup = false;


    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */
/*
            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

            // Do we have the infinite gas plan?
            Purchase infiniteGasPurchase = inventory.getPurchase(SKU_INFINITE_GAS);
            mSubscribedToInfiniteGas = (infiniteGasPurchase != null &&
                    verifyDeveloperPayload(infiniteGasPurchase));
            Log.d(TAG, "User " + (mSubscribedToInfiniteGas ? "HAS" : "DOES NOT HAVE")
                    + " infinite gas subscription.");
            if (mSubscribedToInfiniteGas) mTank = TANK_MAX;

            // Check for gas delivery -- if we own gas, we should fill up the tank immediately
            Purchase gasPurchase = inventory.getPurchase(SKU_GAS);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG, "We have gas. Consuming it.");
                mHelper.consumeAsync(inventory.getPurchase(SKU_GAS), mConsumeFinishedListener);
                return;
            }

            updateUi();
            setWaitScreen(false);

            */

            Log.d(TAG, "Initial inventory query finished; enabling main UI.");
        }
    };



    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain("Error purchasing: " + result);
              //  setWaitScreen(false);
                if(iapBaseListener!=null){
                    iapBaseListener.onBuyDidFail();
                }
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                //setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(strProduct)) {
                // bought 1/4 tank of gas. So consume it.
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                if(isConsumeProduct)
                {
                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }
                if(iapBaseListener!=null){
                    iapBaseListener.onBuyDidFinish();
                }
            }
//            else if (purchase.getSku().equals(SKU_PREMIUM)) {
//                // bought the premium upgrade!
//                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
//                alert("Thank you for upgrading to premium!");
//             //   mIsPremium = true;
//            //    updateUi();
//             //   setWaitScreen(false);
//            }

        }
    };

    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
              //  mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
              //  saveData();
             //   alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            }
            else {
                complain("Error while consuming: " + result);
            }
           // updateUi();
            //setWaitScreen(false);
            Log.d(TAG, "End consumption flow.");
        }
    };

    public  void SetAppKey(String key)
    {
        base64EncodedPublicKey = key;
        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }

        mHelper = new IabHelper(mainActivity, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });

    }
    public void init(Activity activity) {
        mainActivity = activity;
//
//
// //创建谷歌支付帮助类
// mHelper = new IabHelper(this, base64EncodedPublicKey);
// mHelper.enableDebugLogging(true);
// /**
//  * 初始化和连接谷歌服务
//  */
// mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
//     @Override
//     public void onIabSetupFinished(IabResult result) {
//         if (!result.isSuccess()) {
//             Log.e("PayMethodActivity", "Problem setting up In-app Billing: " + result);
//             return;
//         }
//         /**
//          * 初始化成功,记录下
//          */
//         isGooglePlaySetup = true;
//         if (mHelper == null) {
//             return;
//         }
//     }
// });

    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }
    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(mainActivity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    public   void initSDK(android.content.Context context)
    {


    }

    public  void StartBuy(String product, boolean isConsume)
    {
        strProduct = product;
        isConsumeProduct=isConsume;

        // boolean islogin = MiCommplatform.getInstance().isMiAccountLogin();
        // if(islogin){
        //     doBuy(product);
        // }else{
        //     //先登陆
            
        // }


        // launch the gas purchase UI flow.
        // We will be notified of completion via mPurchaseFinishedListener
        //setWaitScreen(true);
      //  Log.d(TAG, "Launching purchase flow for gas.");

        /* TODO: for security, generate your payload here for verification. See the comments on
         *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
         *        an empty string, but on a production app you should carefully generate this. */
        String payload = "";
if(mHelper.mSetupDone==false){
    Log.e(TAG, "google play iap api is not setup ");
    return;
}
        mHelper.launchPurchaseFlow(mainActivity, product, RC_REQUEST,
                mPurchaseFinishedListener, payload);
    }
    public  void RestoreBuy(String product)
    {
    
    }

    public void setListener(IIAPBaseListener listener) {
        iapBaseListener = listener;
    }

 

    public  void doBuy(String product)
    {
       
    } 


     /**
     * 查询是否有未消费的
     */
    public void checkUnconsume() {
//        try {
//            mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
//                @Override
//                public void onQueryInventoryFinished(IabResult result, Inventory inv) {
//                    if (result.isSuccess() && inv.hasPurchase(SKU)) {
//                        //消费, 并下一步, 这里Demo里面我没做提示,将购买了,但是没消费掉的商品直接消费掉, 正常应该
//                        //给用户一个提示,存在未完成的支付订单,是否完成支付
//                        consumeProduct(inv.getPurchase(SKU), false, "消费成功", "消费失败");
//                    } else {
//                        buyProduct();
//                    }
//                }
//            });
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            e.printStackTrace();
//        }
    }


    /**
     * 产品购买
     */
    private void buyProduct() {

//        try {
//            mHelper.launchPurchaseFlow(this, SKU, REQUEST_CODE, new IabHelper.OnIabPurchaseFinishedListener() {
//                @Override
//                public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
//                    if (result.isFailure()) {
//                        Log.e("PayMethodActivity", "Error purchasing: " + result);
//                        //Toast.makeText(DemoPayMethodActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    Log.d(TAG, "Purchase successful.");
//                    if (purchase.getSku().equals(SKU)) {
//                        // bought 1/4 tank of gas. So consume it.
//                        Log.d(TAG, "Purchase is gas. Starting gas consumption.");
//                        //購買成功,調用消耗產品
//                        consumeProduct(purchase, false, "支付成功", "支付失败");
//                    }
//                }
//            }, mDeveloperPayload);
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            e.printStackTrace();
//           // Toast.makeText(DemoPayMethodActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
//        }
    }


    /**
     * 消费掉已购买商品
     *
     * @param purchase
     * @param needNext
     * @param tipmsg1
     * @param tipmsg2
     */
    private void consumeProduct(Purchase purchase, final boolean needNext, final String tipmsg1, final String tipmsg2) {
//        try {
//            mHelper.consumeAsync(purchase, new IabHelper.OnConsumeFinishedListener() {
//                @Override
//                public void onConsumeFinished(Purchase purchase, IabResult result) {
//                    if (mHelper == null) {
//                        return;
//                    }
//                    if (result.isSuccess()) {
//                        Log.e("PayMethodActivity", "Problem setting up In-app Billing: " + result);
//                        if (!needNext) {
//                            //处理内购中断的情况, 仅仅只是消费掉上一次未正常完成的商品
//                          //  Toast.makeText(DemoPayMethodActivity.this, tipmsg1, Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        try {
//                            //向服务器提交内购验证
//                            //UIUtils.showLoadDialog(that, "验证支付结果");
////                            BaseQuestStart.IosneigouIndexGooglePayUrl_(that, purchase.getOriginalJson(), purchase.getSignature());
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//                    } else {
//                      //  Toast.makeText(DemoPayMethodActivity.this, tipmsg2, Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        } catch (IabHelper.IabAsyncInProgressException e) {
//            e.printStackTrace();
//           // Toast.makeText(DemoPayMethodActivity.this, tipmsg2, Toast.LENGTH_SHORT).show();
//        }
    }

    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //     if (mHelper == null) return;
    //     /**
    //      * 将回调交给帮助类来处理, 否则会出现支付正在进行的错误
    //      */
    //     mHelper.handleActivityResult(requestCode, resultCode, data);
    //     super.onActivityResult(requestCode, resultCode, data);
    // }

    // @Override
    // protected void onDestroy() {
    //     super.onDestroy();
    //     /**
    //      * 释放掉资源
    //      */
    //     if (mHelper != null) {
    //         try {
    //             mHelper.dispose();
    //         } catch (IabHelper.IabAsyncInProgressException e) {
    //             e.printStackTrace();
    //         }
    //     }
    //     mHelper = null;
    // }
}
