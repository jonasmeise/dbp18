<html>
<head><title>babble_search</title>
<header> 
Search:
<form method="post" action="./babble_search">
<input type="text" name="search" value="sample: Ich" maxlength="300"/>
<input type="submit" value="Search">
</form>

<br>
<body>
Search result:

<table class="datatable">
    <tr>
        <th>Creator</th>  <th>Text</th> <th>Created</th> <th>Rebabbles</th> <th>Likes</th> <th>Dislikes</th>
    </tr>
    <tr>
    <#list babblelist as babble>
      <td><form align="left"  method="post" action="./user_profile">   
    <input type="submit" name="profileLink" value="${babble.creator}"/>
</form></td> 
<td><form align="left" action="./babble_details?id=${babble.id}"> 
    <input type="submit" name="babbleIDLink" value="${babble.id}"/> ${babble.text}
</form></td> 
<td>${babble.created}</td>
<td>${babble.likes}</td>
<td>${babble.dislikes}</td>
<td>${babble.rebabbles}</td>
    </tr>
    
    
    </#list>
  </table>
</body>
</html>