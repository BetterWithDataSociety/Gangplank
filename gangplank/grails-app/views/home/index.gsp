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
       <table class="table table-bordered">
         <thead>
           <tr><td>File ID</td><td>Filename</td><td>Schema</td></tr>
         </thead>
         <tbody>
           <g:each in="${files}" var="f">
             <tr>
               <td><g:link controller="browse" action="index" params="${[schema:f.schema.name,q:"sourceFile:\"${f.guid}\""]}">${f.guid}</g:link></td>
               <td>${f.filename}</td>
               <td>${f.schema.name}</td>
             </tr>
           </g:each>
         </tbody>
       </table>
     </div>
   </div>
  
  </body>
</html>
