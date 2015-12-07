package tf_web.jp.bluespp.bluetooth;

/**
 * Created by furukawanobuyuki on 2015/12/07.
 */
public interface SppListener {
    /** エラーを通知
     *
     * @param message
     */
    void onError(final String message);

    /** 接続時に通知
     *
     */
    void onConnect();

    /** 閉じる時通知
     *
     */
    void onClose();
}
