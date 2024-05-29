# Refactoring

## ToDo

### Collapse namespaces

Currently there are two modes of operation in two explicit namespaces:
reply mode and account mode.  

This leads to higher maintenance needs and more complex program structure. The overall goal is now to reduce this complexity.  

Having one ns called to_html that produces html as needed seems appropriate at this time.

### CSS per mode

Creating css that does basic styling depending on the mode.

### Automatically connecting CSS and appropriate HTML

