<html> 
<head>
<title>user_profile/${userID}</title> 
 <style type="text/css">
    #outer
{
    width:100%;
    text-align: center;
}
.inner
{
    display: inline-block;
}
    </style>
    
</head>
<body>
<header>
<div id="outer">
<div class="inner">
<form action="./babble_search"> 
    <input type="submit" value="BubbleSearch" />
</form>
</div>

<div class="inner">
<form>
    <button type="submit" name="followButton"/>Follow/Unfollow
</form>
</div>

<div class="inner">
<form>
<button type="submit" name="blockButton"/>Block/Unblocked
</form>


</div>
</div>
</header>

<p>${profilepic}</p>
<p>Benuter:  ${username} </p>
<p>Name: ${name} </p>
<p>Status: ${status}</p>	



<form align="right" action="./babble_create"> 
    <input type="submit" value="New Babble"/>
</form>

<p> ${block} </p>
<p> ${follow} </p>



</body>
</html>