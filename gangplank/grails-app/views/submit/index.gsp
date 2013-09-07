<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Gangplank</title>
    <r:require modules="gangplank"/>
  </head>
  <body>

   <div class="container-fluid">
     <div class="row-fluid">
       <g:form action="processSubmission" method="post" enctype="multipart/form-data" params="${params}">
         <input type="file" id="submissionFile" name="submissionFile"/>
         <button type="submit" class="btn btn-primary">Upload</button>
       </g:form>
     </div>
   </div>
  
  </body>
</html>
