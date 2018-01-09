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
<form action="theia.is.uni-due.de:9018/babble_search"> 
    <input type="submit" value="BubbleSearch" />
</form>
</div>

<div class="inner">
<form>
    <button type="submit"/>Follow/Unfollow
</form>
</div>

<div class="inner">
<form>
    <button type="submit"/>Block/Unblock
</form>
</div>
</div>
</header>

<p>{user.profilepic}</p>
<p>Benuter:  {user.username} </p>
<p>Name: {user.firstname} ${user.lastname}</p>
<p>Status: {user.status}</p>

<form align="right" action="theia.is.uni-due.de:9018/babble_create"> 
    <input type="submit" value="New Babble"/>
</form>

</body>
</html>