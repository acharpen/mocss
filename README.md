# mocss

Mocss is a tool that translates legacy CSS code into Sass by automatically identifying mixins to eliminate duplication in the original CSS file.

It enables a fine-grained control on the generated code:
* Filtering mixins depending on the number of parameters or declarations they introduced,
* Generating a Sass code semantically equivalent to the original CSS.

## How can I use it?

`java -jar mocss.jar -i orgininalFile.css -o generatedFile.css`

The options enabling a control on the generated code are defined below:
```
 --max-parameters VALUE      : avoids mixins having more than VALUE parameters. Lower the value is,
                               less mixins are generated. (default: 6)
 --min-children VALUE        : avoids mixins used less than VALUE times. Lower the value is, less
                               mixins are generated. (default: 2)
 --min-declarations VALUE    : avoids mixins introducing less than VALUE declarations. Lower the
                               value is, less mixins are generated. (default: 4)
 --no-duplicates-ruleset     : avoids duplicated declarations in rulesets. If sets, more mixins are
                               generated. (default: false)
 --preserve-semantic         : preserves semantic of the css input file. If sets, more mixins are
                               generated. (default: false)
```