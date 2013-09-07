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
       <h1>Upload a new file</h1>
       <dl class="dl-horizontal">

         <g:form action="processSubmission" method="post" enctype="multipart/form-data" params="${params}">

           <div class="control-group">
             <dt>File to submit</dt>
             <dd><input type="file" id="submissionFile" name="submissionFile"/></dd>
           </div>

           <div class="control-group">
             <dt>Schema</dt>
             <dd><g:select id="schematype" name='schema.id' 
                   from='${gangplank.Schema.list()}'
                   optionKey="id" optionValue="name"></g:select></dd>
           </div>
           <div class="control-group">
             <dt></dt>
             <dd><button type="submit" class="btn btn-primary">Upload</button></dd>
           </div>
         </dl>
       </g:form>
     </div>
   </div>
  
  </body>
</html>
