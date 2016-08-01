Just a quick example showing GraphQL Json structures being parsed into JsonObjects
from a raw GraphQL request made by Retrofit and handled via RxJava.

The main question around the use of RxJava at the data layer here is why you
wouldn't just use the built-in asynchronous request features of Retrofit2
directly. RxJava is better suited for performing operations on multiple streams
of data making the extra boilerplate worth it for those cases.

With GraphQL, you're usually going to get the entire data structure
in one shot. The majority of the work there is to map the fields to the views.

You could go really quick and dirty and just parse out JsonObjects from the
responses and map the fields directly to the view, or you could use the Gson
converter and supply types to be mapped-to before sending off to the view.

Either way, the schema is the source of truth and if it changes the models/bindings/queries
have to as well. Since we're writing GQL queries directly in as strings,
there's not a lot of boilerplate necessary to get up and running.

The majority of the work on the receiving end would really be the field mapping
in almost any scenario. Doing stream work on the resulting data sets may be
necessary in some cases for the future, but luckily that's an easy thing to just
plug in.

### Usage

`gradle run`

Watch terminal output for stringified JSON object containing `Account` #1 from
the Waldo GraphQL API.
