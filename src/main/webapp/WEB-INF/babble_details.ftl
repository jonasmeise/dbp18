<html>
<head><title>babble_details</title>


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
<input type="submit" name="rebabbleButton" value="${rebabbleValue}"/>
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