package tf_web.jp.bluespp.bluetooth;

/**
 * Created by furukawanobuyuki on 2015/12/07.
 */
public interface SppListener {
    /** エラーを通知
     *
     * @param message
     */
    void onError(String message);
}
