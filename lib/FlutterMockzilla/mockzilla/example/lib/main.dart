import 'package:flutter/material.dart';
import 'package:mockzilla/mockzilla.dart';
import 'package:mockzilla_platform_interface/mockzilla_platform_interface.dart';

const defaultResponse = MockzillaHttpResponse(
  statusCode: 200,
  headers: {},
  body: """{
    "packages": [
      {
        "name": "Mockzilla",
        "description": "Super mocking library!"
      },
      {
        "name": "Flutter",
        "description": "Core Flutter framework"
      }
    ]
  }""",
);

const errorResponse = MockzillaHttpResponse(
  statusCode: 400,
  headers: {},
  body: "",
);

final mockzillaConfig = MockzillaConfig(
  port: 8080,
  endpoints: [
    EndpointConfig(
      name: "Fetch Packages",
      key: "fetch-packages",
      endpointMatcher: (request) => true,
      defaultHandler: (_) => defaultResponse,
      errorHandler: (_) => errorResponse,
      failureProbability: 0,
      delayMean: 100,
      delayVariance: 0,
    ),
  ],
  isRelease: false,
  localHostOnly: false,
  logLevel: LogLevel.debug,
  releaseModeConfig: const ReleaseModeConfig(),
  additionalLogWriters: [],
);

void main() async {
  await WidgetsFlutterBinding.ensureInitialized();
  await Mockzilla.startMockzilla(mockzillaConfig);
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.deepPurple),
        useMaterial3: true,
      ),
      home: const MyHomePage(title: 'Flutter Demo Home Page'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({super.key, required this.title});

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Theme.of(context).colorScheme.inversePrimary,
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headlineMedium,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ),
    );
  }
}
