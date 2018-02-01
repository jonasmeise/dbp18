<html>
<head><title>babble_details</title>
<style type="text/css">
* {
   margin:0;
   padding:0;
}

body{
   text-align:center;
   background: #efe4bf none repeat scroll 0 0;
}

#wrapper{
   width:960px;
   margin:0 auto;
   text-align:left;
   background-color: #fff;
   border-radius: 0 0 10px 10px;
   padding: 20px;
   box-shadow: 1px -2px 14px rgba(0, 0, 0, 0.4);
}

#header{
 color: #fff;
 background-color: #2c5b9c;
 height: 3.5em;
 padding: 1em 0em 1em 1em;
 
}

#site{
    background-color: #fff;
    padding: 20px 0px 0px 0px;
}
.centerBlock{
	margin:0 auto;
}
</style>

<body>

<p> ${creator} </p>
<p> ${text} </p>
<p> Created: ${created} </p>
<p> Likes: ${likes} </p>
<p> Dislikes: ${dislikes} </p>
<p> Rebabbles: ${rebabbles} </p>


<br>
    
    <div id="outer">
        <div class="inner">
<form method="post" action="./babble_details?babbleIDLink=${babbleid}"> 
<input type="submit" name="likeButton" value="Like"/>
</form>
</div>

    <div class="inner">
<form method="post" action="./babble_details?babbleIDLink=${babbleid}"> 
<input type="submit" name="dislikeButton" value="Dislike"/>
</form>
</div>

    <div class="inner">
<form method="post" action="./babble_details?babbleIDLink=${babbleid}"> 
<input type="submit" name="rebabbleButton" value="Rebabble"/>
</form>
</div>
    
    <div class="inner">
<form method="post" action="./babble_details?babbleIDLink=${babbleid}">
<input type="submit" name="deleteButton" value="Delete"/>
</form>
</div>
</div>
    
    <input type="hidden" name="babbleIDLink" id="babbleIDLink" />
    
    
<form align="right" action="./user_profile"> 
    <input type=submit name="back" value="Back to User Profile"/>
</form>
</body>
</html>