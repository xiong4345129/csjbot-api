package com.csjbot.youtu;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.csjbot.util.YoutuSign;
import com.youtu.sign.*;
import java.io.IOException;
import java.io.DataOutputStream;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;



/**
 *
 * @author tyronetao
 */
public class Youtu {

	public final static  String API_YOUTU_END_POINT = "http://service.image.myqcloud.com/face/";
	// 30 days
	private static int EXPIRED_SECONDS = 2592000;
	private String m_appid;
	private String m_buket;
	private String m_secret_id;
	private String m_secret_key;
	private String m_end_point;
	private String m_user_id;

	/**
	 * PicCloud 构造方法
	 *
	 * @param appid
	 *            授权appid
	 * @param secret_id
	 *            授权secret_id
	 * @param secret_key
	 *            授权secret_key
	 */
	/**
	 * 
	 */
	public Youtu() {
		// TODO Auto-generated constructor stub
	}
	public Youtu(String appid,String buket, String secret_id, String secret_key,String end_point,String user_id) {
		m_appid = appid;
		m_buket = buket;
		m_secret_id = secret_id;
		m_secret_key = secret_key;
		m_end_point=end_point;
		m_user_id=user_id;
	}

	public String StatusText(int status) {

		String statusText = "UNKOWN";

        switch (status)
        {
        	case 0:
                statusText = "CONNECT_FAIL";
                break;
            case 200:
                statusText = "HTTP OK";
                break;
            case 400:
                statusText = "BAD_REQUEST";
                break;
            case 401:
                statusText = "UNAUTHORIZED";
                break;
            case 403:
                statusText = "FORBIDDEN";
                break;
            case 404:
                statusText = "NOTFOUND";
                break;
            case 411:
                statusText = "REQ_NOLENGTH";
                break;
            case 423:
                statusText = "SERVER_NOTFOUND";
                break;
            case 424:
                statusText = "METHOD_NOTFOUND";
                break;
            case 425:
                statusText = "REQUEST_OVERFLOW";
                break;
            case 500:
                statusText = "INTERNAL_SERVER_ERROR";
                break;
            case 503:
                statusText = "SERVICE_UNAVAILABLE";
                break;
            case 504:
                statusText = "GATEWAY_TIME_OUT";
                break;
        }
        return statusText;
	}


	private void GetBase64FromFile(String filePath, StringBuffer base64)
	throws IOException {
		File imageFile = new File(filePath);
		if (imageFile.exists()) {
			InputStream in = new FileInputStream(imageFile);
			byte data[] = new byte[(int) imageFile.length()]; // 创建合适文件大小的数组
			in.read(data); // 读取文件中的内容到b[]数组
			in.close();
			base64.append(Base64Util.encode(data));

		} else {
			throw new FileNotFoundException(filePath + " not exist");
		}

	}

	private JSONObject SendHttpRequest(JSONObject postData, String mothod)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

		StringBuffer mySign = new StringBuffer("");
		YoutuSign.appSign(m_appid,m_buket,m_secret_id, m_secret_key,
			System.currentTimeMillis() / 1000 + EXPIRED_SECONDS,
			m_user_id, mySign);
		

		System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
		System.setProperty("sun.net.client.defaultReadTimeout", "30000");
		URL url = new URL(m_end_point + mothod);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		// set header
		connection.setRequestMethod("POST");
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("user-agent", "youtu-java-sdk");
		connection.setRequestProperty("Authorization", mySign.toString());

		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type", "text/json");
		connection.connect();

		// POST请求
		DataOutputStream out = new DataOutputStream(
			connection.getOutputStream());

		postData.put("app_id", m_appid);
		out.write(postData.toString().getBytes("utf-8"));
		//out.writeBytes(postData.toString());
		out.flush();
		out.close();
		// 读取响应
		BufferedReader reader = new BufferedReader(new InputStreamReader(
			connection.getInputStream()));
		String lines;
		StringBuffer resposeBuffer = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			resposeBuffer.append(lines);
		}
		// System.out.println(resposeBuffer+"\n");
		reader.close();
		// 断开连接
		connection.disconnect();

		JSONObject respose = JSONObject.parseObject(resposeBuffer.toString());

		return respose;

	}

	/**
	 * 人脸检测
	 * 描述：给定一个图片判断人脸面部特征。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     image/url: 图片内容或地址
	 */
	public JSONObject DetectFace(String imageByte,int mode) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {

		//StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		//GetBase64FromFile(imageByte, image_data);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("image", imageByte);
		data.put("mode", mode);
		
		JSONObject respose =SendHttpRequest(data, "detect");

		return respose;
	}

	/**
	 * 人脸检测
	 * 描述：给定一个图片判断人脸面部特征。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     image/url: 图片内容或地址
	 */
	public JSONObject DetectFaceUrl(String url, int mode)
	throws IOException, JSONException, KeyManagementException,
	NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		data.put("mode", mode);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		JSONObject respose =SendHttpRequest(data, "detect");
		return respose;
	}

	public JSONObject FaceShape(String image_path,int mode) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException  {

		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		data.put("mode", mode);
		JSONObject respose =SendHttpRequest(data, "faceshape");

		return respose;
	}

	public JSONObject FaceShapeUrl(String url,int mode) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException  {

		JSONObject data = new JSONObject();
		data.put("url", url);
		data.put("mode", mode);
		JSONObject respose =SendHttpRequest(data, "faceshape");

		return respose;
	}

	public JSONObject FaceCompare(String image_path_a, String image_path_b)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path_a, image_data);
		data.put("imageA", image_data.toString());
		image_data.setLength(0);

		GetBase64FromFile(image_path_b, image_data);
		data.put("imageB", image_data.toString());

		JSONObject respose =SendHttpRequest(data, "facecompare");

		return respose;
	}

	public JSONObject FaceCompareUrl(String urlA, String urlB)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

		JSONObject data = new JSONObject();

		data.put("urlA", urlA);
		data.put("urlB", urlB);

		JSONObject respose =SendHttpRequest(data, "facecompare");

		return respose;
	}
	/**
	 * 人脸验证
	 * 描述：给定一个图片和一个Person，检查是否是同一个人。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     person_id: 人ID
	 *     image/url: 图片内容或地址
	 */
	public JSONObject FaceVerify(String image,String person_id)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();

		data.put("image", image);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);

		JSONObject respose =SendHttpRequest(data, "faceverify");

		return respose;
	}
	/**
	 * 人脸验证
	 * 描述：给定一个图片和一个Person，检查是否是同一个人。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     person_id: 人ID
	 *     image/url: 图片内容或地址
	 */
	public JSONObject FaceVerifyUrl(String url,String person_id)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

		JSONObject data = new JSONObject();

		data.put("url", url);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);

		JSONObject respose =SendHttpRequest(data, "faceverify");

		return respose;
	}
	/**
	 * 人脸检索
	 * 描述：对于一个待识别的人脸图片，在一个Group中识别出最相似的Top5 Person作为其身份返回，返回的Top5中按照相似度从大到小排列。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     group_id:  候选人组ID
	 *     image/url: 图片内容或地址
	 */
	public JSONObject FaceIdentify(String image, String group_id)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();

		data.put("image", image);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);

		data.put("group_id", group_id);

		JSONObject respose =SendHttpRequest(data, "identify");

		return respose;
	}
	/**
	 * 人脸检索
	 * 描述：对于一个待识别的人脸图片，在一个Group中识别出最相似的Top5 Person作为其身份返回，返回的Top5中按照相似度从大到小排列。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     group_id:  候选人组ID
	 *     image/url: 图片内容或地址
	 */
	public JSONObject FaceIdentifyUrl(String url, String group_id)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		data.put("group_id", group_id);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		
		JSONObject respose =SendHttpRequest(data, "identify");

		return respose;
	}

	/**
	 * 个体创建
	 * 描述：创建一个Person，并将Person放置到group_ids指定的组当中，不存在的group_id会自动创建。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     group_ids: 候选人组ID list数组
	 *     image/url: 图片内容或地址
	 * 
	 */
	public JSONObject NewPerson(String image, String person_id,
		List<String> group_ids,String person_name) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("image", image);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);
		data.put("group_ids", group_ids);
		data.put("person_name", person_name);

		JSONObject respose =SendHttpRequest(data, "newperson");

		return respose;
	}
	
	/**
	 * 个体创建
	 * 描述：创建一个Person，并将Person放置到group_ids指定的组当中，不存在的group_id会自动创建。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     group_ids: 候选人组ID list数组
	 *     image/url: 图片内容或地址
	 * 
	 */
	public JSONObject NewPersonUrl(String url, String person_id,
		List<String> group_ids,String person_name) throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);
		data.put("group_ids", group_ids);
		data.put("person_name", person_name);

		JSONObject respose =SendHttpRequest(data, "newperson");

		return respose;
	}
	/**
	 * 删除个体
	 * 描述：删除一个Person。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     person_id: 个体ID
	 * 
	 */
	public JSONObject DelPerson(String person_id) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {

		JSONObject data = new JSONObject();
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);

		JSONObject respose =SendHttpRequest(data, "delperson");

		return respose;
	}
	/**
	 * 添加一个人脸
	 * 描述：将一组Face加入到一个Person中。注意，一个Face只能被加入到一个Person中。 一个Person最多允许包含20个Face；加入几乎相同的人脸会返回错误。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     person_id: 个体ID
	 *     image/url: 图片或地址的list数组
	 * 
	 */
	public JSONObject AddFace(String person_id, List<String> image)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("images", image);
		data.put("person_id", person_id);

		JSONObject respose =SendHttpRequest(data, "addface");

		return respose;
	}
	/**
	 * 添加一个人脸
	 * 描述：将一组Face加入到一个Person中。注意，一个Face只能被加入到一个Person中。 一个Person最多允许包含20个Face；加入几乎相同的人脸会返回错误。
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     person_id: 个体ID
	 *     image/url: 图片或地址的list数组
	 * 
	 */
	public JSONObject AddFaceUrl(String person_id, List<String> url_arr)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("urls", url_arr);
		data.put("person_id", person_id);

		JSONObject respose =SendHttpRequest(data, "addface");

		return respose;
	}
	/**
	 * 删除人脸
	 * 描述：删除人脸
	 * 参数：appid:     项目ID
	 *     bucket:    项目名称
	 *     person_id: 个体ID
	 *     face_ids:  人脸ID的list数组
	 * 
	 */
	public JSONObject DelFace(String person_id, List<String> face_id_arr)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {

		JSONObject data = new JSONObject();
		
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("face_ids", face_id_arr);
		data.put("person_id", person_id);
		JSONObject respose =SendHttpRequest(data, "delface");

		return respose;

	}
	/**
	 * 设置信息
	 * 描述：设置person的name值
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 *     person_id:   个体ID
	 *     person_name: person的name（初始化默认未空）
	 * 
	 */
	public JSONObject SetInfo(String person_name, String person_id)
	throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_name", person_name);
		data.put("person_id", person_id);
		JSONObject respose =SendHttpRequest(data, "setinfo");

		return respose;

	}
	/**
	 * 获取信息
	 * 描述：获取一个Person的信息, 包括name, id, tag, 相关的face, 以及groups等信息
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 *     person_id:   个体ID
	 */
	public JSONObject GetInfo(String person_id) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);
		JSONObject respose =SendHttpRequest(data, "getinfo");

		return respose;
	}
	/**
	 * 获取组列表
	 * 描述：获取一个AppId下所有group列表。
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 */
	public JSONObject GetGroupIds() throws IOException, JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		JSONObject respose =SendHttpRequest(data, "getgroupids");

		return respose;
	}
	/**
	 * 获取人列表
	 * 描述：获取一个组Group中所有person列表。
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 *     group_id:    组ID
	 * 
	 */
	public JSONObject GetPersonIds(String group_id) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("group_id", group_id);
		JSONObject respose =SendHttpRequest(data, "getpersonids");

		return respose;
	}
	/**
	 * 获取人脸列表
	 * 描述：获取一个组person中所有face列表。
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 *     person_id:   个体ID
	 * 
	 */
	public JSONObject GetFaceIds(String person_id) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("person_id", person_id);
		JSONObject respose =SendHttpRequest(data, "getfaceids");

		return respose;
	}
	/**
	 * 获取人脸信息
	 * 描述：获取一个face的相关特征信息。
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 *     face_id:     人脸ID
	 * 
	 */
	public JSONObject GetFaceInfo(String face_id) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		data.put("face_id", face_id);
		JSONObject respose =SendHttpRequest(data, "getfaceinfo");

		return respose;
	}
	/**
	 * 设置信息
	 * 描述：设置person的name值
	 * 参数：appid:       项目ID
	 *     bucket:      项目名称
	 *     person_id:   个体ID
	 *     person_name: person的name（初始化默认未空）
	 * 
	 */

	public JSONObject FuzzyDetect(String image_path) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		data.put("appid", m_appid);
		data.put("bucket", m_buket);
		JSONObject respose =SendHttpRequest(data, "imageapi/fuzzydetect");

		return respose;
	}
	
	public JSONObject FuzzyDetectUrl(String url,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();

		data.put("url", url);
		JSONObject respose =SendHttpRequest(data, "imageapi/fuzzydetect");
		return respose;
	}
	
	public JSONObject FoodDetect(String image_path,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		JSONObject respose =SendHttpRequest(data, "fooddetect");
		return respose;
	}
	
	public JSONObject FoodDetectUrl(String url,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();

		data.put("url", url);
		JSONObject respose =SendHttpRequest(data, "fooddetect");
		return respose;
	}
	

	public JSONObject ImageTag(String image_path,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		JSONObject respose =SendHttpRequest(data, "imagetag");
		return respose;
	}
	
	public JSONObject ImageTagUrl(String url,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		JSONObject respose =SendHttpRequest(data, "imagetag");
		return respose;
	}


	public JSONObject ImagePorn(String image_path,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {

		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		JSONObject respose =SendHttpRequest(data, "imageporn");
		return respose;
	}

	public JSONObject ImagePornUrl(String url,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		JSONObject respose =SendHttpRequest(data, "imageporn");
		return respose;
	}

	public JSONObject IdCardOcr(String image_path,int card_type,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		data.put("card_type", card_type);
		JSONObject respose =SendHttpRequest(data, "idcardocr");
		return respose;
	}

	public JSONObject IdCardOcrUrl(String url,int card_type,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		data.put("card_type", card_type);
		JSONObject respose =SendHttpRequest(data, "idcardocr");
		return respose;
	}

	public JSONObject NameCardOcr(String image_path,boolean retimage,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {

		StringBuffer image_data = new StringBuffer("");
		JSONObject data = new JSONObject();

		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		data.put("retimage", retimage);
		JSONObject respose =SendHttpRequest(data, "namecardocr");
		return respose;
	}

	public JSONObject NameCardOcrUrl(String url,boolean retimage,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("url", url);
		data.put("retimage", retimage);
		JSONObject respose =SendHttpRequest(data, "namecardocr");
		return respose;
	}



	public JSONObject LiveGetFour(String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		JSONObject respose =SendHttpRequest(data, "livegetfour");
		return respose;
	}


	public JSONObject LiveDetectFour(String validate_data,String video_path,String card_path,boolean compare_card,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("validate_data", validate_data);

		StringBuffer video_data = new StringBuffer("");
		GetBase64FromFile(video_path, video_data);
		data.put("video", video_data.toString());

		if(compare_card)
		{
			StringBuffer card_data = new StringBuffer("");
			GetBase64FromFile(card_path, card_data);
			data.put("card", card_data.toString());
			data.put("compare_flag", true);
		}
		else
		{
			data.put("compare_flag", false);
		}


		JSONObject respose =SendHttpRequest(data, "livedetectfour");
		return respose;
	}

	public JSONObject IdcardLiveDetectfour(String idcard_number,String idcard_name,String validate_data,String video_path,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("idcard_number", idcard_number);
		data.put("idcard_name", idcard_name);
		data.put("validate_data", validate_data);

		StringBuffer video_data = new StringBuffer("");
		GetBase64FromFile(video_path, video_data);
		data.put("video", video_data.toString());

		JSONObject respose =SendHttpRequest(data, "idcardlivedetectfour");
		return respose;
	}

	public JSONObject IdcardFaceCompare(String idcard_number,String idcard_name,String image_path,String appid,String bucket) throws IOException,
	JSONException, KeyManagementException, NoSuchAlgorithmException {
		JSONObject data = new JSONObject();
		data.put("idcard_number", idcard_number);
		data.put("idcard_name", idcard_name);
		StringBuffer image_data = new StringBuffer("");
		GetBase64FromFile(image_path, image_data);
		data.put("image", image_data.toString());
		data.put("sep", "sdf");

		JSONObject respose =SendHttpRequest(data, "idcardfacecompare");
		return respose;
	}
	


}
