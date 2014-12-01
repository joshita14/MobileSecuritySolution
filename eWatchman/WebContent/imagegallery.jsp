<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

 <%@ page import="java.util.*" %>
 <%@ page import="com.iiit.HelperClass" %>
 <%@ page import="com.iiit.CloudStorageAPI" %>
 <%@ page import="java.io.File" %>
<html>

<head>
  <link rel="stylesheet" href="css/style2.css">


<title>EWatchman Image Gallery</title>


</head>

<script>
function showImage(imgName) {
    document.getElementById('largeImg').src = imgName;
    showLargeImagePanel();
    unselectAll();
}
function showLargeImagePanel() {
    document.getElementById('largeImgPanel').style.visibility = 'visible';
}
function unselectAll() {
    if(document.selection) document.selection.empty();
    if(window.getSelection) window.getSelection().removeAllRanges();
}
function hideMe(obj) {
    obj.style.visibility = 'hidden';
}
</script>

<body>

<div align="left"><Font size=3 ><a href=home.jsp>HOME</Font></a><div>
<div align="right"><Font size=3><a  href=login.jsp >LOGOUT</Font></a></div>


<div class="gallery" align="center">


<%
    String bucketName = request.getParameter("selectedbucket");
    String date = request.getParameter("date2");
    Date date1=HelperClass.parseDate(date);
    
    File file=new File("H:\\workspace\\EWatchman\\WebContent\\Babies\\");
	CloudStorageAPI.getImages(bucketName, date1, file.getAbsolutePath());
%>    

<h3>Images On <%=date %></h3><br/>

<div class="thumbnails" >

<%

   File f=new File("H:\\workspace\\EWatchman\\WebContent\\Babies\\");
   List<String> filenamesFromBucket=HelperClass.listFilesForFolder(f);
   
   for(String filename : filenamesFromBucket){

 %>
    <img  src="<%=filename%>" style="cursor:pointer" onclick="showImage('<%=filename%>');">
   <%
       }
   %>

<div id="largeImgPanel" onclick="hideMe(this);">
<img id="largeImg" style="height: 100%; margin: 0; padding: 0;">
</div>
</div>


</body>

</html>
