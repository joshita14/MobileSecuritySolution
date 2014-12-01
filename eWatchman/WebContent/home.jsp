<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

 <%@ page import="java.util.List" %>
 <%@ page import="com.iiit.HelperClass" %>
 <%@ page import="com.iiit.CloudStorageAPI" %>
 <%@ page import="java.io.File" %>
<html>

<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>Dark Login Form</title>
  <link rel="stylesheet" href="css/style.css">
  <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
	<!--Load Script and Stylesheet -->
	<script type="text/javascript" src="js/jquery.simple-dtpicker.js"></script>
	<link type="text/css" href="css/jquery.simple-dtpicker.css" rel="stylesheet" />
	<!---->
	
	<style type="text/css">
		body { background-color: #fefefe; padding-left: 2%; padding-bottom: 100px; color: #101010; }
		footer{ font-size:small;position:fixed;right:5px;bottom:5px; }
		a:link, a:visited  { color: #0000ee; }
		pre{ background-color: #eeeeee; margin-left: 1%; margin-right: 2%; padding: 2% 2% 2% 5%; }
		p { font-size: 0.9rem; }
		ul { font-size: 0.9rem; }
		hr { border: 2px solid #eeeeee; margin: 2% 0% 2% -3%; }
		h3 { border-bottom: 2px solid #eeeeee; margin: 2rem 0 2rem -1%; padding-left: 1%; padding-bottom: 0.1em; }
		h4 { border-bottom: 1px solid #eeeeee; margin-top: 2rem; margin-left: -1%; padding-left: 1%; padding-bottom: 0.1em; }
	</style>
  
</head>

<body>
  <form method="post" action="imagegallery.jsp" class="home">
    <p>
     <label >DeviceName</label> </t>
     <%
     List<String> bucketNames = CloudStorageAPI.getBuckets();
     System.out.println(bucketNames);
     %>
     <select name="selectedbucket">
          <%
             for(String bucknm : bucketNames){
               System.out.println(" We are  herrerre !!!!!!!!!!!!!" + bucknm);
		   %>
			    <option value="<%=bucknm%>">
			        <%=bucknm %>
			    </option>
            <%
                 }
             %>
     </select> 
      
      <br>
      <br>
      <br>
	<input type="text" name="date2" value="">
		<script type="text/javascript">
			$(function(){
				$('*[name=date2]').appendDtpicker({
					"inline": true,
					"dateFormat": "DD.MM.YYYY hh:mm"
				});
			});
		</script>
	 
	 <BODY>
	 <%
	    File file=new File("H:\\workspace\\EWatchman\\WebContent\\Babies\\");
	    HelperClass help=new HelperClass();
	    help.delete(file);
	  %>
	        <button type="submit" class="login-button">Login</button>
    </p>
<!--
    <p class="forgot-password"><a href="index.html">Forgot your password?</a></p> -->
  </form>
</body>
</html>
