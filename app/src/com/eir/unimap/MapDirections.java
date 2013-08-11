/** Author: Eirini Televantou* This class was developed By using code found online - http://stackoverflow.com/questions/14495030/google-map-api-v2-get-driving-directions 
 *  Year: 2013
 *  Organization: University of Southampton
 *  Info: This is the class were the directions are calculated 
 *  **/

package com.eir.unimap;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


public class MapDirections {
public final static String MODE_WALKING = "walking";


public MapDirections() {
}

public Document getDocument(LatLng start, LatLng end, String mode) {
    String url = "http://maps.googleapis.com/maps/api/directions/xml?"
            + "origin=" + start.latitude + "," + start.longitude
            + "&destination=" + end.latitude + "," + end.longitude
            + "&sensor=false&units=metric&mode=walking";
    Log.d("url", url);
    try {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        HttpPost httpPost = new HttpPost(url);
        HttpResponse res = httpClient.execute(httpPost, localContext);
        InputStream is = res.getEntity().getContent();
        DocumentBuilder b= DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        Document doc = b.parse(is);
        return doc;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}


 
public ArrayList<LatLng> getDirection(Document doc) {
    NodeList n1, n2, n3;
    ArrayList<LatLng> points = new ArrayList<LatLng>();
    n1 = doc.getElementsByTagName("step");
    if (n1.getLength() > 0) {
        for (int i = 0; i < n1.getLength(); i++) {
            Node nd1 = n1.item(i);
            n2 = nd1.getChildNodes();

            Node locNode = n2
                    .item(getNodeIndex(n2, "start_location"));
            n3 = locNode.getChildNodes();
            Node latNode = n3.item(getNodeIndex(n3, "lat"));
            double lat = Double.parseDouble(latNode.getTextContent());
            Node lngNode = n3.item(getNodeIndex(n3, "lng"));
            double lng = Double.parseDouble(lngNode.getTextContent());
           points.add(new LatLng(lat, lng));

            locNode = n2.item(getNodeIndex(n2, "polyline"));
            n3 = locNode.getChildNodes();
            latNode = n3.item(getNodeIndex(n3, "points"));
            ArrayList<LatLng> arr = decodePoly(latNode.getTextContent());
            for (int j = 0; j < arr.size(); j++) {
                points.add(new LatLng(arr.get(j).latitude, arr
                        .get(j).longitude));
            }

            locNode = n2.item(getNodeIndex(n2, "end_location"));
            n3 = locNode.getChildNodes();
            latNode = n3.item(getNodeIndex(n3, "lat"));
            lat = Double.parseDouble(latNode.getTextContent());
            lngNode = n3.item(getNodeIndex(n3, "lng"));
            lng = Double.parseDouble(lngNode.getTextContent());
           points.add(new LatLng(lat, lng));
        }
    }

    return points;
}

private int getNodeIndex(NodeList nl, String nodename) {
    for (int i = 0; i < nl.getLength(); i++) {
        if (nl.item(i).getNodeName().equals(nodename))
            return i;
    }
    return -1;
}

private ArrayList<LatLng> decodePoly(String encoded) {
    ArrayList<LatLng> poly = new ArrayList<LatLng>();
    int index = 0, len = encoded.length();
    int lat = 0, lng = 0;
    while (index < len) {
        int b, shift = 0, result = 0;
        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lat += dlat;
        shift = 0;
        result = 0;
        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);
        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lng += dlng;

        LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
        poly.add(position);
    }
    return poly;
}
}