# mocss

Mocss is a tool that translates legacy CSS code into Sass by automatically identifying mixins to eliminate duplication in the original CSS file. It generates a Sass code semantically equivalent to the original CSS file.

It enables a fine-grained control on the generated code, in particular:
* Filtering mixins depending on the kind of properties they factorized,
* Filtering mixins depending on the number of parameters or declarations they introduced.

## How can I use it?

`java -jar mocss.jar -i orgininalFile.css -o generatedFile.scss`

The options enabling a control on the generated code are defined below:
```
 --debug                                : debug mode (default: false)
 --groups-filter                        : generates mixins factorizing common properties. (default:
                                          false)
 --keep-semantic [full | slight | none] : determines whether semantics of the input file has to be
                                          preserved.
                                          full: semantics is preserved and additional mixins are
                                          generated to avoid duplication;
                                          slight: semantics is preserved without new mixins;
                                          none: semantics is not preserved. (default: full)
 --max-parameters VALUE                 : avoids mixins having more than VALUE parameters. Lower
                                          the value is, less mixins are generated. (default: 1)
 --min-children VALUE                   : avoids mixins used less than VALUE times. Lower the value
                                          is, more mixins are generated. (default: 2)
 --min-declarations VALUE               : avoids mixins introducing less than VALUE declarations.
                                          Lower the value is, more mixins are generated. (default:
                                          3)
 --no-duplicates-into-rule              : avoids duplicated declarations in ruleset. If sets, more
                                          mixins are generated. (default: false)
 -f (--output-format) [SCSS]            : language of the output file:  (default: SCSS)
 -h (--help)                            : help (default: false)
 -i (--input) FILE                      : the css input file
 -o (--output) FILE                     : the output file
```
