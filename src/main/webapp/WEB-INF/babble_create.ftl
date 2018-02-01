<html>
<head><title>babble_create</title>
<body>

<br>
<form method="post" action=./babble_create>
<textarea name="textarea" rows="4" cols="50" maxlength="280"> </textarea>
    <input type="submit" name="createButton" value="Post Babble" />
    $('.character-limit').keyup(function(){ 
  var limit = 280; // Maximale Anzahl an Zeichen  
  var count = $(this).val().length; 
  $('.counter').html(count);
</form>
<form align="right" action="./user_profile"> 
    <input type=submit name="back" value="Back to User Profile"/>
</form>
</body>
</html>