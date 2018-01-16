<html> 
<head>
<title>user_profile</title> 
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

<button onclick="block()">Block/Unblocked</button>



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

<p id=blockedString></p>

<script>
function block() {
    var blocked;
    if (${userID}.equals(FooBar) {
    blocked ="You are not blocked.";
    } else {
        blocked = "You are blocked.";
    }
    document.getElementById(blockedString).innerHTML = blocked;
}
</script>

</body>
</html>