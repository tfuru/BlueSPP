package tf_web.jp.bluespp.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

/**
 * Created by furukawanobuyuki on 2015/12/07.
 */
public class Spp {
    private static final String TAG = "Spp";

    //SPPサービスのUUID
    private static final UUID SPP_SERVICE_ID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Context context;
    private SppListener listener;

    //接続先デバイス名
    private String deviceName;

    public Spp(String deviceName,final Context context,final SppListener listener) {
        Log.d(TAG, "Spp()");
        this.context = context;
        this.listener = listener;
        this.setDeviceName(deviceName);
    }

    /** デバイス名を外から設定
     *
     * @param deviceName
     */
    public void setDeviceName(final String deviceName){
        this.deviceName = deviceName.trim();
    }

    /** USERID
     *
     */
    public void writeUserID() {
        Log.d(TAG,"writeUserID()");

        BluetoothDevice bluetoothDevice = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            listener.onMessage("Bluetooth is disabled.");
            return;
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice dev : bondedDevices) {
            String name = dev.getName();
            if(name != null) {
                Log.d(TAG, "name:" + name + " " + dev.getAddress());
                if (name.equals(deviceName)) {
                    bluetoothDevice = dev;
                }
            }
        }
        if(bluetoothDevice == null){
            String msg = "bluetoothDevice is null. deviceName:"+deviceName;
            listener.onMessage(msg);
            return;
        }

        try {
            //SPP 接続の為のソケットを取得
            BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SPP_SERVICE_ID);
            if(bluetoothSocket == null){
                listener.onMessage("bluetoothSocket is null");
                return;
            }

            //接続
            bluetoothSocket.connect();
            listener.onMessage("Connect");

            //書き込み
            BufferedOutputStream bos = new BufferedOutputStream(bluetoothSocket.getOutputStream());

            //ユーザーIDを書き込む
            // sendCommand(Array(BTProtocol.P_START, BTProtocol.USERID, id.length.toByte) ++ id ++ Array(BTProtocol.P_END))
            byte[] cmd_userid = new byte[]{
                                    (byte)0xff,
                                    (byte)0x81,
                                    (byte)0x09, //サイズ指定をミスらなければ正常に書き込める
                                    (byte)0x20,(byte)0x9A,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x50,
                                    (byte)0xfe};


            bos.write(cmd_userid);
            bos.flush();

            bos.close();

            //切断
            bluetoothSocket.close();
            listener.onMessage("Close");
        }
        catch (IOException ex) {

        }
    }

    /** ADVERTISE
     *
     */
    public void writeAdvertise() {
        Log.d(TAG,"writeAdvertise()");

        BluetoothDevice bluetoothDevice = null;
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            String msg = "Bluetooth is disabled.";
            listener.onMessage(msg);
            return;
        }
        Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice dev : bondedDevices) {
            String name = dev.getName();
            if(name != null){
                Log.d(TAG, "name:" + name + " " +dev.getAddress());
                if (name.equals(deviceName)) {
                    bluetoothDevice = dev;
                }
            }
        }
        if(bluetoothDevice == null){
            String msg = "bluetoothDevice is null";
            listener.onMessage(msg);
            return;
        }

        try {
            //SPP 接続の為のソケットを取得
            BluetoothSocket bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(SPP_SERVICE_ID);
            if(bluetoothSocket == null){
                String msg = "bluetoothSocket is null";
                listener.onMessage(msg);
                return;
            }

            //接続
            bluetoothSocket.connect();
            listener.onMessage("Connect");

            //書き込み
            BufferedOutputStream bos = new BufferedOutputStream(bluetoothSocket.getOutputStream());

            //Array(BTProtocol.P_START, BTProtocol.ADVERTISE, BTProtocol.LEN_1, Constants.ADVERTISE_INVITATION_SERVICE.toByte, BTProtocol.P_END)
            byte[] cmd_advertise = new byte[]{
                    (byte)0xff,
                    (byte)0x04,
                    (byte)0x01,
                    (byte)0x02,
                    (byte) 0xfe};
            bos.write(cmd_advertise);
            bos.flush();

            bos.close();

            //切断
            bluetoothSocket.close();
            listener.onMessage("Close");
        }
        catch (IOException ex) {

        }
    }
}
