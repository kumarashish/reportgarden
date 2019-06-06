package dagger_component;

import dagger.Component;
import network.ApiCall;

@Component(modules= AppModule.class)
public interface AppComponent {
  ApiCall getAPIService();
}
