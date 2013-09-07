<!DOCTYPE html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Gangplank</title>
    <r:require modules="gangplank"/>
  </head>
  <body>

   <div class="container-fluid">

     <h1>Schema Props</h1>

     <table class="table table-bordered table-striped">
       <thead>
         <tr>
           <td>Label</td>
           <td>Property Name</td>
           <td>Required?</td>
           <td>Rules</td>
         </tr>
       </thead>
       <tbody>
         <g:each in="${schema.props}" var="p">
           <tr>
             <td>${p.label}</td>
             <td>${p.name}</td>
             <td>${p.mandatory}</td>
             <td></td>
           </tr>
         </g:each>
       </tbody>
     </table>
   </div>
  
  </body>
</html>
