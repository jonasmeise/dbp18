<html> 
<body>
<p> Babbles: </p>
<p>${blockedReason} </p>
<div style=${blockedStatus}>
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
  </div>
  </body>
</html>