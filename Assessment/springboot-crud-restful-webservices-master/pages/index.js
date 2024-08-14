function myFunction() {
  var a = document.getElementById("mytext").value;
  var Details = new Object();
  Details.data = a;
  $.ajax({
  type:'POST',
  url: "http://127.0.0.1:8080/api/users/uploadMessage",
  data: a,
  cache: true,
  dataType:"json",
  success: function(html){
	  alert("Successfully sent to RabbitMQ");
  }
});
  
  
}