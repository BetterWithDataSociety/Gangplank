package gangplank

class RefdataValue {

  String value

  // N.B. This used to be ICON but in the 2.x series this was changed to be a css class which denotes an icon
  // Please stick with the change to store css classes in here and not explicit icons
  String icon

  static belongsTo = [
    owner:RefdataCategory
  ]

  static mapping = {
         id column:'rdv_id'
    version column:'rdv_version'
      owner column:'rdv_owner', index:'rdv_entry_idx'
      value column:'rdv_value', index:'rdv_entry_idx'
       icon column:'rdv_icon'
  }

  static constraints = {
    icon(nullable:true)
  }

  static def refdataFind(params) {
    def result = [];
    def ql = null;
    ql = RefdataValue.findAllByValueIlike("%${params.q}%",params)

    if ( ql ) {
      ql.each { id ->
        result.add([id:"${id.class.name}:${id.id}",text:"${id.value}"])
      }
    }

    result
  }

}
