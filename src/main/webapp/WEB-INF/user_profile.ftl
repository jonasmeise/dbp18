<html> 
<head>
<title>user_profile/</title> 
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
<form method="post" action="./user_profile"> 
<input type="submit" name="follow" value="Follow/Unfollow" />
</form>
</div>

<div class="inner">
<form method="post" action="./user_profile">
<button type="submit" name="block" value="Block/Unblocked" />
</form>
</div>

<div class="inner">
<form method="post" action="./user_profile"> 
<input type="submit" name="MyPage" value="FooBar"/>
</form>
</div>

</div>
</header>

<p>${foto}</p>
<p>Benuter:  ${username} </p>
<p>Name: ${name} </p>
<p>Status: ${status}</p>	



<form align="right" action="./babble_create"> 
    <input type="submit" value="New Babble"/>
</form>

<p> ${block} , because ${reason} </p>
<p> ${follow} </p>
</br>
</br>
<p> Babbles: </p>

<table class="datatable">
    <tr>
        <th>Creator</th>  <th>Text</th> <th>Created</th> <th>Rebabbles</th> <th>Likes</th> <th>Dislikes</th>
    </tr>
    <tr>
    <#list babblelist as babble>
      <td><form align="left"  method="post" action="./user_profile">   
    <input type="submit" name="profileLink" value="${babble.creator}"/>
</form></td> 
<td><form align="left" action="./babble_details"> 
    <input type="submit" name="babbleIDLink" value="${babble.id}"/> ${babble.text}
</form></td> 
<td>${babble.created}</td>
<td>${babble.likes}</td>
<td>${babble.dislikes}</td>
<td>${babble.rebabbles}</td>
    </tr>
    
    
    </#list>
  </table>

</br>
</body>
</html>