package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;
import static edu.rice.pcdp.PCDP.finish;
/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 *
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     *
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor sieveActor = new SieveActorActor(2);
        finish(() -> {
                        //sieveActor.start();
                        for(int i=3;i<=limit;i+=2)
                        {
                            sieveActor.send(i);
                        }
        
        //sieveActor.send(0);
        });

        int numPrimes = 0;
        SieveActorActor loopActor = sieveActor;
        while(loopActor!= null)
        {
            numPrimes +=1;
            loopActor = loopActor.nextActor;
        }

	return numPrimes;
        //throw new UnsupportedOperationException();
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {
        /**
         * Process a single message sent to this actor.
         *
         * TODO complete this method.
         *
         * @param msg Received message
         */
        private int localprime;
        private SieveActorActor nextActor;

        SieveActorActor(final int prime)
        {
            this.localprime = prime;
        }
        @Override
        public void process(final Object msg) 
        {
            int cand = (Integer) msg;
            boolean nonmul = ((cand%localprime)!=0);
            if(nonmul)
            {
                if(nextActor == null)
                {
                    nextActor = new SieveActorActor(cand);
                }

                else
                {
                    nextActor.send(msg);
                }
            }
        }
    }
}
