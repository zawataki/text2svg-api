# NOTE: This API is unavailable now due to [Heroku free plan ending](https://blog.heroku.com/next-chapter).

# text2svg-api
[![Build Status](https://travis-ci.com/zawataki/text2svg-api.svg?branch=master)](https://travis-ci.com/zawataki/text2svg-api)

API for converting text to SVG

# How to use

## Convert from text
If you write like this,

```html
<img src='https://text2svg-api.herokuapp.com/svg?text=Hello%20world%21%0d%0aYes,%20You%20Can!!' />
```

show like the following:

<img src='https://text2svg-api.herokuapp.com/svg?text=Hello%20world%21%0d%0aYes,%20You%20Can!!' />

## Convert from URL
If you write like this,

```html
<img src='https://text2svg-api.herokuapp.com/svg?url=https://raw.githubusercontent.com/zawataki/text2svg-api/master/README.md' height="500" />
```

show like the following:

<img src='https://text2svg-api.herokuapp.com/svg?url=https://raw.githubusercontent.com/zawataki/text2svg-api/master/README.md' height="200%" width="200%" />

### Convert from URL with line number
If you write like this,

```html
<img src='https://text2svg-api.herokuapp.com/svg?url=https://raw.githubusercontent.com/zawataki/text2svg-api/master/README.md&line=2-6' />
```

show like the following:

<img src='https://text2svg-api.herokuapp.com/svg?url=https://raw.githubusercontent.com/zawataki/text2svg-api/master/README.md&line=2-6' />

