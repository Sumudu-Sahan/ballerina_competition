routeManagerApp.service("routeManagerService", function ($http) {

	this.getLocations = function(){
		return [
              {
                  city : 'Lotus Road',
                  desc : 'This is the best country in the world!',
                  lat : 6.931379319889805,
                  long : 79.84250664710999
              },
              {
                  city : 'Pettah',
                  desc : 'The Heart of India!',
                  lat : 6.934137771440814,
                  long : 79.84850406646729
              },
              {
                  city : 'Manning Market',
                  desc : 'Bollywood city!',
                  lat : 6.934755491968783,
                  long : 79.85291361808777
              },
              {
                  city : 'Olcot Market',
                  desc : 'Howrah Bridge!',
                  lat : 6.932114198521828,
                  long : 79.85767722129822
              },
              {
                  city : 'Sri Sangaraja',
                  desc : 'Kathipara Bridge!',
                  lat : 6.932976880671076,
                  long : 79.86328840255737
              }
          ];
	}
});