import 'package:mockzilla/mockzilla.dart';

extension MockzillaConfigExtensions on MockzillaConfig {
  MockzillaConfig addEndpoint(EndpointConfig Function() builder) =>
      copyWith(endpoints: endpoints + [builder()]);
}
