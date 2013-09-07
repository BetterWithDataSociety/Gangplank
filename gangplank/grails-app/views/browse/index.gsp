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
       ... Browse ${params}
       <table class="table table-bordered">
         <thead>
           <tr>
             <g:each in="${schemaInfo.props}" var="prop">
               <td>${prop.label}</td>
             </g:each>
             <td>Timestamp</td>
             <td>Source File</td>
           </tr>
         </thead>
         <tbody>
           <g:each in="${hits}" var="h">
             <tr>
               <g:each in="${schemaInfo.props}" var="prop">
                 <td>${h.source[prop.name]}</td>
               </g:each>
               <td>${h.source.gangplankTimestamp}</td>
               <td>${h.source.sourceFile}</td>
             </tr>
           </g:each>
         </tbody>
       </table>
     </div>
   </div>
  
  </body>
</html>
