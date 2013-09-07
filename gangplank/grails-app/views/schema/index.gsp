<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Gangplank</title>
    <r:require modules="gangplank"/>
  </head>
  <body>

   <div class="container-fluid">

     <h1>List of registered schemas</h1>

     <table>
       <thead>
         <tr>
           <td>Schema name</td>
         </tr>
       </thead>
       <tbody>
         <g:each in="${schemas}" var="s">
           <tr>
             <td>${s.name}</td?
           </tr>
         </g:each>
       </body>
     </table>
   </div>
  
  </body>
</html>
